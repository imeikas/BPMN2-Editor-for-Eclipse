/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
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
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.util.ErrorUtils;
import org.eclipse.bpmn2.modeler.ui.wizards.BPMN2DiagramCreator;
import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
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
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;

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
		super.init(site, input);
	}

	private void getModelPathFromInput(DiagramEditorInput input) {
		URI uri = input.getDiagram().eResource().getURI();

		uri = uri.trimFragment();
		uri = uri.trimFileExtension();
		uri = uri.appendFileExtension("bpmn2");

		String uriString = uri.toPlatformString(true).replace(".bpmn2/", "");
		URI.createURI(uriString);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		modelFile = root.getFile(new Path(uriString));
	}

	/**
	 * Beware, creates a new input and changes this editor!
	 */
	private IEditorInput createNewDiagramEditorInput() throws CoreException {
		IPath fullPath = modelFile.getFullPath();
		modelUri = URI.createPlatformResourceURI(fullPath.toString(), true);

		IFolder folder = BPMN2DiagramCreator.getTempFolder(fullPath);
		diagramFile = BPMN2DiagramCreator
				.getTempFile(fullPath.removeFileExtension().addFileExtension("bpmn2d"), folder);

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

	@Override
	public void dispose() {
		super.dispose();
		ModelHandlerLocator.releaseModel(modelUri);
	}

	public IFile getModelFile() {
		return modelFile;
	}
}
