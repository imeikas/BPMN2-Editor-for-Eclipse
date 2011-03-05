package org.jboss.bpmn2.editor.ui.editor;

import java.io.IOException;

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

/**
 * @author if
 * 
 */
@SuppressWarnings("restriction")
public class BPMN2Editor extends DiagramEditor {

	public static String EDITOR_ID = "org.jboss.bpmn2.editor.ui.bpmn2editor";

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
		IEditorInput input;
		BPMN2DiagramCreator creator = new BPMN2DiagramCreator();
		IPath fullPath = modelFile.getFullPath();
		modelUri = URI.createPlatformResourceURI(fullPath.toString(), true);

		IFolder folder = getTempFolder(fullPath);
		diagramFile = getTempFile(fullPath.removeFileExtension().addFileExtension("bpmn2d"), folder);

		// Create new temporary diagram file
		creator.setDiagramFile(diagramFile);
		input = creator.createDiagram(false);

		diagramUri = creator.getUri();
		return input;
	}

	private IFile getTempFile(IPath fullPath, IFolder folder) {
		IFile tempFile = folder.getFile(fullPath.lastSegment());

		// We don't need anything from that file and to be sure there are no side effects we delete the file
		if (tempFile.exists()) {
			try {
				tempFile.delete(true, null);
			} catch (CoreException e) {
				Activator.showErrorWithLogging(e);
			}
		}
		return tempFile;
	}

	private IFolder getTempFolder(IPath fullPath) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		IFolder folder = root.getProject(fullPath.segment(0)).getFolder(".bpmn2");
		if (!folder.exists()) {
			folder.create(true, true, null);
		}
		String[] segments = fullPath.segments();
		for (int i = 1; i < segments.length - 1; i++) {
			String segment = segments[i];
			folder = folder.getFolder(segment);
			if (!folder.exists()) {
				folder.create(true, true, null);
			}
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

			ResourceSet diagramResourceSet = ((DiagramEditorInput) input).getDiagram().eResource().getResourceSet();
			ResourceSet resourceSet = getEditingDomain().getResourceSet();
			Bpmn2ResourceImpl bpmnResource = (Bpmn2ResourceImpl) resourceSet.createResource(modelUri,
					"org.eclipse.bpmn2.content-type.xml");
			try {
				if (modelFile.exists()) {
					bpmnResource.load(null);
				}
			} catch (IOException e) {
				Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
				ErrorUtils.showErrorWithLogging(status);
			}
			modelHandler = ModelHandlerLocator.createModelHandler(modelUri, bpmnResource);
			ModelHandlerLocator.put(diagramUri, modelHandler);
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
		ModelHandlerLocator.releaseModel(modelUri);
	}

	public IFile getModelFile() {
		return modelFile;
	}
}
