package org.jboss.bpmn2.editor.ui.editor;

import java.io.File;
import java.io.IOException;

import org.eclipse.bpmn2.util.Bpmn2ResourceImpl;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.ui.IEditorInput;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.core.di.DIImport;
import org.jboss.bpmn2.editor.ui.Activator;
import org.jboss.bpmn2.editor.ui.util.ErrorUtils;

@SuppressWarnings("restriction")
public class BPMN2Editor extends DiagramEditor {

	public static String EDITOR_ID = "org.jboss.bpmn2.editor.ui.bpmn2editor";

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
			String modelsPath = diagramPath.substring(0, diagramPath.length() - 1);

			modelPath = URI.createURI(modelsPath);

			ResourceSet resourceSet = getEditingDomain().getResourceSet();
			Bpmn2ResourceImpl resource = (Bpmn2ResourceImpl) resourceSet.createResource(modelPath,
					"org.eclipse.bpmn2.content-type.xml");
			try {
				File file = new File(modelsPath);
				if (file.exists()) {
					resource.load(null);
				}
			} catch (IOException e) {
				Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
				ErrorUtils.showErrorWithLogging(status);
			}
			modelHandler = ModelHandlerLocator.createModelHandler(modelPath, resource);
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
	}
}
