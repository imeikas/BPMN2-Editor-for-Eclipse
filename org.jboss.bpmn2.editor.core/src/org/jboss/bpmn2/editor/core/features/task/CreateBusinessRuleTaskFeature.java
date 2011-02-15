package org.jboss.bpmn2.editor.core.features.task;

import org.eclipse.bpmn2.BusinessRuleTask;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateBusinessRuleTaskFeature extends AbstractCreateFlowElementFeature<BusinessRuleTask> {

	public CreateBusinessRuleTaskFeature(IFeatureProvider fp) {
	    super(fp, "Business Rule Task", "Task that can use Business Rules Engine");
    }

	@Override
    protected BusinessRuleTask createFlowElement(ICreateContext context) {
		BusinessRuleTask task = ModelHandler.FACTORY.createBusinessRuleTask();
		task.setName("Business Rule Task");
		task.setImplementation("##unspecified");
		return task;
    }
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_BUSINESS_RULE_TASK;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); //FIXME
	}
}