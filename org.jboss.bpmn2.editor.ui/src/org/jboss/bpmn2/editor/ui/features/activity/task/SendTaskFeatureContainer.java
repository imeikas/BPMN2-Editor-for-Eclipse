package org.jboss.bpmn2.editor.ui.features.activity.task;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.SendTask;
import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.activity.task.AbstractCreateTaskFeature;
import org.jboss.bpmn2.editor.core.features.activity.task.AbstractTaskFeatureContainer;
import org.jboss.bpmn2.editor.core.features.activity.task.AddTaskFeature;
import org.jboss.bpmn2.editor.core.utils.GraphicsUtil;
import org.jboss.bpmn2.editor.ui.ImageProvider;

public class SendTaskFeatureContainer extends AbstractTaskFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof SendTask;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateSendTaskFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddTaskFeature(fp) {
			@Override
			protected void decorateActivityRectangle(RoundedRectangle rect) {
				IGaService service = Graphiti.getGaService();
				Image img = service.createImage(rect, ImageProvider.IMG_16_SEND_TASK);
				service.setLocationAndSize(img, 2, 2, GraphicsUtil.TASK_IMAGE_SIZE, GraphicsUtil.TASK_IMAGE_SIZE);
			}
		};
	}

	public static class CreateSendTaskFeature extends AbstractCreateTaskFeature {

		public CreateSendTaskFeature(IFeatureProvider fp) {
			super(fp, "Send Task", "Task that is completed when a message is sent");
		}

		@Override
		protected Task createFlowElement(ICreateContext context) {
			SendTask task = ModelHandler.FACTORY.createSendTask();
			task.setName("Send Task");
			task.setImplementation("##unspecified");
			return task;
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_SEND_TASK;
		}
	}
}