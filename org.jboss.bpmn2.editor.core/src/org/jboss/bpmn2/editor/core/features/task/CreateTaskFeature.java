package org.jboss.bpmn2.editor.core.features.task;

import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateTaskFeature extends AbstractCreateFlowElementFeature<Task> {
	
	public CreateTaskFeature(IFeatureProvider fp) {
		super(fp, "Task", "Create Task");
	}
	
	@Override
    protected Task createFlowElement(ICreateContext context) {
		Task task = ModelHandler.FACTORY.createTask();
		if(support.isTargetLane(context)) {
			Lane lane = (Lane) getBusinessObjectForPictogramElement(context.getTargetContainer());
			task.getLanes().add(lane);
		}
		task.setName("Task Name");
		return task;
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
