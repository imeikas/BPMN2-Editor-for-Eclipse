package org.jboss.bpmn2.editor.core.features.task;

import java.io.IOException;

import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;

public class CreateTaskFeature extends AbstractCreateFeature {

	public CreateTaskFeature(IFeatureProvider fp) {
		super(fp, "Task", "Create Task");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		return context.getTargetContainer() instanceof Diagram;
	}

	@Override
	public Object[] create(ICreateContext context) {
		final String name = "Task Name";

		Task x = null;
		try {
			ModelHandler mh = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
			x = mh.addFlowElement(ModelHandler.FACTORY.createTask());
			x.setName(name);
		} catch (IOException e) {
			Activator.logError(e);
		}

		addGraphicalRepresentation(context, x);

		return new Object[] { x };
	}

	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_TASK;
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); // FIXME
	}
}
