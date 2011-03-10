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
package org.jboss.bpmn2.editor.ui.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
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
import org.jboss.bpmn2.editor.ui.Activator;
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
		createDiagram(true);
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

	public static IFolder getTempFolder(IPath fullPath) throws CoreException {
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

	public static IFile getTempFile(IPath fullPath, IFolder folder) {
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

}
