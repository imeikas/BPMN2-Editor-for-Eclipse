package org.jboss.bpmn2.editor.core.features.task;

import org.eclipse.bpmn2.ReceiveTask;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateReceiveTaskFeature extends AbstractCreateFlowElementFeature<ReceiveTask> {
	
	public CreateReceiveTaskFeature(IFeatureProvider fp) {
	    super(fp, "Receive Task", "Task that is completed when a message arrives");
    }

	@Override
	protected ReceiveTask createFlowElement(ICreateContext context) {
		ReceiveTask task = ModelHandler.FACTORY.createReceiveTask();
		task.setName("Receive Task");
		task.setImplementation("##unspecified");
		return task;
	}
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_RECEIVE_TASK;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); //FIXME
	}
}