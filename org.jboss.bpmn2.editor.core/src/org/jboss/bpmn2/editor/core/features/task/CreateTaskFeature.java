package org.jboss.bpmn2.editor.core.features.task;

import java.io.IOException;

import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public class CreateTaskFeature extends AbstractCreateFeature {
	
	private FeatureSupport support = new FeatureSupport() {
		@Override
		protected Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};
	
	public CreateTaskFeature(IFeatureProvider fp) {
		super(fp, "Task", "Create Task");
	}
	
	@Override
	public boolean canCreate(ICreateContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = support.isTargetLane(context) && support.isTargetLaneOnTop(context); 
		return intoDiagram || intoLane;
	}

	@Override
	public Object[] create(ICreateContext context) {
		final String name = "Task Name";

		Task task = null;
		try {
			ModelHandler mh = support.getModelHanderInstance(getDiagram());
			task = mh.addFlowElement(ModelHandler.FACTORY.createTask());
			task.setName(name);
			if(support.isTargetLane(context)) {
				task.getLanes().add((Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
			}
		} catch (IOException e) {
			Activator.logError(e);
		}

		addGraphicalRepresentation(context, task);

		return new Object[] { task };
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
