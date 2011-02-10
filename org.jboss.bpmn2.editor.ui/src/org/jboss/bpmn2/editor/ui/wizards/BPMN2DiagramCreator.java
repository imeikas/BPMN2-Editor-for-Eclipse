package org.jboss.bpmn2.editor.ui.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.examples.common.FileService;
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

	private IProject project;
	private IFolder diagramFolder;
	private IFile diagramFile;

	public void createExample() throws CoreException {
		if (diagramFolder != null && !diagramFolder.exists())
			diagramFolder.create(false, true, null);

		Diagram diagram = Graphiti.getPeCreateService().createDiagram("BPMN2", diagramFile.getName(), true);
		URI uri = URI.createPlatformResourceURI(diagramFile.getFullPath().toString(), true);
		
		//FIXME: rewrite domain creation so we can drop dependency to Graphiti Example project
		TransactionalEditingDomain domain = FileService.createEmfFileForDiagram(uri, diagram);
		
		String providerId = GraphitiUi.getExtensionManager().getDiagramTypeProviderId(diagram.getDiagramTypeId());
		final DiagramEditorInput editorInput = new DiagramEditorInput(EcoreUtil.getURI(diagram), domain, providerId);

		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			public void run() {
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editorInput, BPMN2Editor.EDITOR_ID);

				} catch (PartInitException e) {
					String error = "Error while opening diagram editor";
					IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, error, e);
					ErrorUtils.showErrorWithLogging(status);
				}
			}
		});
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
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

}
