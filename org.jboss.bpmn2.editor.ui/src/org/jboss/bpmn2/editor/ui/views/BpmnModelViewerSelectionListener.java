package org.jboss.bpmn2.editor.ui.views;

import java.io.IOException;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.ui.editor.BPMN2Editor;

class BpmnModelViewerSelectionListener implements ISelectionListener {
	private final ViewContentProvider contentProvider;
	private BPMN2Editor editor;
	private final TreeViewer viewer;

	public BpmnModelViewerSelectionListener(TreeViewer viewer) {
		this.viewer = viewer;
		this.contentProvider = (ViewContentProvider) viewer.getContentProvider();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		if (part instanceof BPMN2Editor) {
			editor = (BPMN2Editor) part;
			try {
				ModelHandler modelHandler = ModelHandlerLocator.getModelHandler(editor.getDiagramTypeProvider()
						.getDiagram().eResource());
				contentProvider.updateModel(modelHandler);
				viewer.refresh(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Object[] selected = contentProvider.getSelected(selection);
		if (selected != null) {
			viewer.setSelection(new StructuredSelection(selected), true);
		}
	}
}
