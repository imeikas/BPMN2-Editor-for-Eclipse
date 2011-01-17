package org.jboss.bpmn2.editor.ui.editor;

import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.ui.IEditorInput;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class BPMN2Editor extends DiagramEditor {

	private ModelHandler modelHandler;
	private URI modelPath;

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

			DiagramEditorInput dInput = (DiagramEditorInput) input;
			String diagramPath = dInput.getUriString().split("#")[0];
			String modelsPath = diagramPath.substring(0,
					diagramPath.length() - 1);

			modelPath = URI.createURI(modelsPath);

			ResourceSet resourceSet = getEditingDomain().getResourceSet();
			Bpmn2ResourceImpl resource = (Bpmn2ResourceImpl) resourceSet
					.createResource(modelPath,
							"org.eclipse.bpmn2.content-type.xml");
			modelHandler = ModelHandler.createModelHandler(modelPath, resource);
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		ModelHandler.releaseModel(modelPath);
	}
}
