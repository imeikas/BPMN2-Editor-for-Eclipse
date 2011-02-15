package org.jboss.bpmn2.editor.core.features.task;

import org.eclipse.bpmn2.SendTask;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateSendTaskFeature extends AbstractCreateFlowElementFeature<SendTask> {

	public CreateSendTaskFeature(IFeatureProvider fp) {
	    super(fp, "Send Task", "Task that is completed when a message is sent");
    }

	@Override
    protected SendTask createFlowElement(ICreateContext context) {
	    SendTask task = ModelHandler.FACTORY.createSendTask();
	    task.setName("Send Task");
	    task.setImplementation("##unspecified");
	    return task;
    }
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_SEND_TASK;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); //FIXME
	}
}
