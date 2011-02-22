package org.jboss.bpmn2.editor.core.features.task;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class UserTaskFeatureContainer extends AbstractTaskFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof UserTask;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateUserTaskFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddTaskFeature(fp) {
			@Override
			protected void decorateTask(RoundedRectangle rect, IAddContext context) {
				IGaService service = Graphiti.getGaService();
				Image img = service.createImage(rect, ImageProvider.IMG_16_USER_TASK);
				service.setLocationAndSize(img, 0, 0, 16, 16);
			}
		};
	}

	public static class CreateUserTaskFeature extends AbstractCreateTaskFeature {

		public CreateUserTaskFeature(IFeatureProvider fp) {
			super(fp, "User Task", "Task performed by human");
		}

		@Override
		protected Task createFlowElement(ICreateContext context) {
			UserTask task = ModelHandler.FACTORY.createUserTask();
			task.setName("User Task");
			return task;
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_USER_TASK;
		}
	}
}