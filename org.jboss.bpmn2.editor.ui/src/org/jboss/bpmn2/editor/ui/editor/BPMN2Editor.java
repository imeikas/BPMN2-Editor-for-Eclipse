package org.jboss.bpmn2.editor.ui.editor;

import java.io.IOException;

import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.core.di.DIImport;
import org.jboss.bpmn2.editor.ui.Activator;
import org.jboss.bpmn2.editor.ui.util.ErrorUtils;
import org.jboss.bpmn2.editor.ui.wizards.BPMN2DiagramCreator;

@SuppressWarnings("restriction")
public class BPMN2Editor extends DiagramEditor {

	public static String EDITOR_ID = "org.jboss.bpmn2.editor.ui.bpmn2editor";

	private ModelHandler modelHandler;
	private URI modelPath;
	private URI diagramPath;

	private IFile modelFile;
	private IFile diagramFile;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		try {
			if (input instanceof IFileEditorInput) {
				BPMN2DiagramCreator creator = new BPMN2DiagramCreator();

				modelFile = ((IFileEditorInput) input).getFile();
				IPath fullPath = modelFile.getFullPath();
				modelPath = URI.createPlatformResourceURI(fullPath.toString(), true);

				IFolder folder = getTempFolder(fullPath);
				diagramFile = getTempFile(fullPath, folder);

				// Create new temporary diagram file
				creator.setDiagramFile(diagramFile);
				input = creator.createDiagram(false);

				diagramPath = creator.getUri();
			} else if (input instanceof DiagramEditorInput) {
				String uriString = ((DiagramEditorInput) input).getUriString();
				System.out.println(uriString);
			}
		} catch (CoreException e) {
			Activator.showErrorWithLogging(e);
		}
		super.init(site, input);
	}

	private IFile getTempFile(IPath fullPath, IFolder folder) {
		IFile tempFile = folder.getFile(fullPath.lastSegment());
		if (tempFile.exists()) {
			// FIXME: create a new temp file
		}
		return tempFile;
	}

	private IFolder getTempFolder(IPath fullPath) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IFolder folder = root.getProject(fullPath.segment(0)).getFolder(".bpmn2");
		if (!folder.exists()) {
			folder.create(true, true, null);
		}
		return folder;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO: this might be unnecessary, if all elements eClasses would have ID's
		modelHandler.save();
		super.doSave(monitor);
	}

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		if (input instanceof DiagramEditorInput) {

			ResourceSet resourceSet = getEditingDomain().getResourceSet();
			Bpmn2ResourceImpl resource = (Bpmn2ResourceImpl) resourceSet.createResource(modelPath,
					"org.eclipse.bpmn2.content-type.xml");
			try {
				if (modelFile.exists()) {
					resource.load(null);
				}
			} catch (IOException e) {
				Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
				ErrorUtils.showErrorWithLogging(status);
			}
			modelHandler = ModelHandlerLocator.createModelHandler(modelPath, resource);
			ModelHandlerLocator.put(diagramPath, modelHandler);
			importDiagram();
			((BasicCommandStack) getEditingDomain().getCommandStack()).saveIsDone();
		}
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
		ModelHandlerLocator.releaseModel(modelPath);
		if (diagramFile != null && diagramFile.exists()) {
			try {
				diagramFile.delete(true, null);
			} catch (CoreException e) {
				Activator.showErrorWithLogging(e);
			}
		}
	}
}
