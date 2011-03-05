package org.jboss.bpmn2.editor.ui.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.ui.editor.BPMN2Editor;
import org.jboss.bpmn2.editor.ui.util.ErrorUtils;

/**
 * 
 */
public class BPMN2DiagramCreator {

	private IFolder diagramFolder;
	private IFile diagramFile;
	private URI uri;

	public void createDiagram() throws CoreException {
		final DiagramEditorInput editorInput = createDiagram(true);
	}

	public DiagramEditorInput createDiagram(boolean openEditor) throws CoreException {
		if (diagramFolder != null && !diagramFolder.exists()) {
			diagramFolder.create(false, true, null);
		}

		final Diagram diagram = Graphiti.getPeCreateService().createDiagram("BPMN2",
				diagramFile.getFullPath().removeFileExtension().lastSegment(), true);
		uri = URI.createPlatformResourceURI(diagramFile.getFullPath().toString(), true);

		TransactionalEditingDomain domain = FileService.createEmfFileForDiagram(uri, diagram);

		String providerId = GraphitiUi.getExtensionManager().getDiagramTypeProviderId(diagram.getDiagramTypeId());
		final DiagramEditorInput editorInput = new DiagramEditorInput(EcoreUtil.getURI(diagram), domain, providerId);

		if (openEditor) {
			openEditor(editorInput);
		}

		return editorInput;
	}

	private void openEditor(final DiagramEditorInput editorInput) {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
							.openEditor(editorInput, BPMN2Editor.EDITOR_ID);

				} catch (PartInitException e) {
					String error = "Error while opening diagram editor";
					IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, error, e);
					ErrorUtils.showErrorWithLogging(status);
				}
			}
		});
	}

	public IFolder getDiagramFolder() {
		return diagramFolder;
	}

	public void setDiagramFolder(IFolder diagramFolder) {
		this.diagramFolder = diagramFolder;
	}

	public IFile getDiagramFile() {
		return diagramFile;
	}

	public void setDiagramFile(IFile diagramFile) {
		this.diagramFile = diagramFile;
	}

	public URI getUri() {
		return uri;
	}

}
