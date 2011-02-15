package org.jboss.bpmn2.editor.core.features.task;

import org.eclipse.bpmn2.ServiceTask;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateServiceTaskFeature extends AbstractCreateFlowElementFeature<ServiceTask> {

	public CreateServiceTaskFeature(IFeatureProvider fp) {
	    super(fp, "Service Task", "Task that uses some kind of service");
    }

	@Override
    protected ServiceTask createFlowElement(ICreateContext context) {
	    ServiceTask task = ModelHandler.FACTORY.createServiceTask();
	    task.setName("Service Task");
	    task.setImplementation("##unspecified");
	    return task;
    }
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_SERVICE_TASK;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); //FIXME
	}
}
