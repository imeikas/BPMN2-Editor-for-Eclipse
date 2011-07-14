/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.editor;

import java.io.IOException;

import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.ProxyURIConverterImplExtension;
import org.eclipse.bpmn2.modeler.core.di.DIImport;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.util.ErrorUtils;
import org.eclipse.bpmn2.modeler.ui.wizards.BPMN2DiagramCreator;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.ExceptionHandler;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain.Lifecycle;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

/**
 * 
 */
@SuppressWarnings("restriction")
public class BPMN2Editor extends DiagramEditor {

	public static String EDITOR_ID = "org.eclipse.bpmn2.modeler.ui.bpmn2editor";

	private ModelHandler modelHandler;
	private URI modelUri;
	private URI diagramUri;

	private IFile modelFile;
	private IFile diagramFile;
	
	private IWorkbenchListener workbenchListener;
	private boolean workbenchShutdown = false;
	
	private BPMN2EditingDomainListener editingDomainListener;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		try {
			if (input instanceof IFileEditorInput) {
				modelFile = ((IFileEditorInput) input).getFile();
				input = createNewDiagramEditorInput();

			} else if (input instanceof DiagramEditorInput) {
				getModelPathFromInput((DiagramEditorInput) input);

				// This was incorrectly constructed input, we ditch the old one and make a new and clean one instead
				input = createNewDiagramEditorInput();
			}
		} catch (CoreException e) {
			Activator.showErrorWithLogging(e);
		}
		
		// add a listener so we get notified if the workbench is shutting down.
		// in this case we don't want to delete the temp file!
		addWorkbenchListener();
		
		super.init(site, input);
	}

	private void getModelPathFromInput(DiagramEditorInput input) {
		URI uri = input.getDiagram().eResource().getURI();
		String uriString = uri.trimFragment().toPlatformString(true);
		modelFile = BPMN2DiagramCreator.getModelFile(new Path(uriString));
	}

	/**
	 * Beware, creates a new input and changes this editor!
	 */
	private IEditorInput createNewDiagramEditorInput() throws CoreException {
		IPath fullPath = modelFile.getFullPath();
		modelUri = URI.createPlatformResourceURI(fullPath.toString(), true);

		IFolder folder = BPMN2DiagramCreator.getTempFolder(fullPath);
		diagramFile = BPMN2DiagramCreator.getTempFile(fullPath,folder);

		// Create new temporary diagram file
		BPMN2DiagramCreator creator = new BPMN2DiagramCreator();
		creator.setDiagramFile(diagramFile);

		IEditorInput input = creator.createDiagram(false);
		diagramUri = creator.getUri();

		return input;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		modelHandler.save();
		((BasicCommandStack) getEditingDomain().getCommandStack()).saveIsDone();
	}

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		
		// Hook a transaction exception handler so we can get diagnostics about EMF validation errors.
		getEditingDomainListener();
		
		BasicCommandStack basicCommandStack = (BasicCommandStack) getEditingDomain().getCommandStack();

		if (input instanceof DiagramEditorInput) {
			ResourceSet resourceSet = getEditingDomain().getResourceSet();
			Bpmn2ResourceImpl bpmnResource = (Bpmn2ResourceImpl) resourceSet.createResource(modelUri,
					"org.eclipse.bpmn2.content-type.xml");

			resourceSet.setURIConverter(new ProxyURIConverterImplExtension());

			modelHandler = ModelHandlerLocator.createModelHandler(modelUri, bpmnResource);
			ModelHandlerLocator.put(diagramUri, modelHandler);

			try {
				if (modelFile.exists()) {
					bpmnResource.load(null);
				} else {
					doSave(null);
				}
			} catch (IOException e) {
				Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
				ErrorUtils.showErrorWithLogging(status);
			}
			basicCommandStack.execute(new RecordingCommand(getEditingDomain()) {

				@Override
				protected void doExecute() {
					importDiagram();
				}
			});
		}
		basicCommandStack.saveIsDone();
	}

	private void importDiagram() {
		DIImport di = new DIImport();
		di.setDiagram(getDiagramTypeProvider().getDiagram());
		di.setDomain(getEditingDomain());
		di.setModelHandler(modelHandler);
		di.setFeatureProvider(getDiagramTypeProvider().getFeatureProvider());
		di.generateFromDI();
	}

	private void addWorkbenchListener() {
		if (workbenchListener==null) {
			workbenchListener = new IWorkbenchListener() {
				@Override
				public boolean preShutdown(IWorkbench workbench, boolean forced) {
					workbenchShutdown = true;
					return true;
				}

				@Override
				public void postShutdown(IWorkbench workbench) {
				}

			};
			PlatformUI.getWorkbench().addWorkbenchListener(workbenchListener);
		}
	}
	
	private void removeWorkbenchListener()
	{
		if (workbenchListener!=null) {
			PlatformUI.getWorkbench().removeWorkbenchListener(workbenchListener);
			workbenchListener = null;
		}
	}
	
	public BPMN2EditingDomainListener getEditingDomainListener() {
		if (editingDomainListener==null) {
			TransactionalEditingDomainImpl editingDomain = (TransactionalEditingDomainImpl)getEditingDomain();
			if (editingDomain==null) {
				return null;
			}
			editingDomainListener = new BPMN2EditingDomainListener(this);

			Lifecycle domainLifeCycle = (Lifecycle) editingDomain.getAdapter(Lifecycle.class);
			domainLifeCycle.addTransactionalEditingDomainListener(editingDomainListener);
		}
		return editingDomainListener;
	}
	
	public BasicDiagnostic getDiagnostics() {
		return getEditingDomainListener().getDiagnostics();
	}
	
	public void showErrorMessage(String msg) {
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();
		IWorkbenchPart part = page.getActivePart();
		IWorkbenchPartSite site = part.getSite();
		IViewSite vSite = ( IViewSite ) site;
		IActionBars actionBars =  vSite.getActionBars();

		if( actionBars == null )
			return;

		IStatusLineManager statusLineManager = actionBars.getStatusLineManager();
		if( statusLineManager == null )
			return;
		
		statusLineManager.setErrorMessage(msg);
		statusLineManager.markDirty();
		statusLineManager.update(true);
	}
	
	@Override
	public void dispose() {
		// clear ID mapping tables if no more instances of editor are active
		int instances = 0;
		IWorkbenchPage[] pages = getEditorSite().getWorkbenchWindow().getPages();
		for (IWorkbenchPage p : pages) {
			IEditorReference[] refs = p.getEditorReferences();
			instances += refs.length;
		}
		ModelUtil.clearIDs(modelHandler.getResource(), instances==0);
		super.dispose();
		ModelHandlerLocator.releaseModel(modelUri);
		// get rid of temp files and folders, but only if the workbench is being shut down.
		// when the workbench is restarted, we need to have those temp files around!
		if (!workbenchShutdown)
			BPMN2DiagramCreator.dispose(diagramFile);
		removeWorkbenchListener();
	}

	public IFile getModelFile() {
		return modelFile;
	}
}
