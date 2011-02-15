package org.jboss.bpmn2.editor.core.features.task;

import org.eclipse.bpmn2.ManualTask;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateManualTaskFeature extends AbstractCreateFlowElementFeature<ManualTask> {

	public CreateManualTaskFeature(IFeatureProvider fp) {
		super(fp, "Manual Task",
		        "Task that is expected to perform without the aid of any business process execution engine or any application");
	}

	@Override
	protected ManualTask createFlowElement(ICreateContext context) {
		ManualTask task = ModelHandler.FACTORY.createManualTask();
		task.setName("Manual Task");
		return task;
	}
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_MANUAL_TASK;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); // FIXME
	}
}
