package org.jboss.bpmn2.editor.core.features.task;

import org.eclipse.bpmn2.UserTask;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateUserTaskFeature extends AbstractCreateFlowElementFeature<UserTask> {

	public CreateUserTaskFeature(IFeatureProvider fp) {
		super(fp, "User Task", "Task performed by human");
	}

	@Override
	protected UserTask createFlowElement(ICreateContext context) {
		UserTask task = ModelHandler.FACTORY.createUserTask();
		task.setName("User Task");
		return task;
	}

	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_USER_TASK;
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); // FIXME
	}
}
