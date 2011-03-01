package org.jboss.bpmn2.editor.core.features.activity.task;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class TaskFeatureContainer extends AbstractTaskFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof Task;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateTaskFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddTaskFeature(fp);
	}

	public static class CreateTaskFeature extends AbstractCreateTaskFeature {

		public CreateTaskFeature(IFeatureProvider fp) {
			super(fp, "Task", "Create Task");
		}

		@Override
		protected Task createFlowElement(ICreateContext context) {
			Task task = ModelHandler.FACTORY.createTask();
			task.setName("Task Name");
			return task;
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_TASK;
		}
	}
}