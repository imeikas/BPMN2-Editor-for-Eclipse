package org.jboss.bpmn2.editor.core.features.activity.task;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BusinessRuleTask;
import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class BusinessRuleTaskFeatureContainer extends AbstractTaskFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof BusinessRuleTask;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateBusinessRuleTaskFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddTaskFeature(fp) {
			@Override
			protected void decorateActivityRectangle(RoundedRectangle rect) {
				IGaService service = Graphiti.getGaService();
				Image img = service.createImage(rect, ImageProvider.IMG_16_BUSINESS_RULE_TASK);
				service.setLocationAndSize(img, 0, 0, 16, 16);
			}

			@Override
			protected int getWidth() {
				return SizeConstants.WIDTH + 50;
			}
		};
	}

	public static class CreateBusinessRuleTaskFeature extends AbstractCreateTaskFeature {

		public CreateBusinessRuleTaskFeature(IFeatureProvider fp) {
			super(fp, "Business Rule Task", "Task that can use Business Rules Engine");
		}

		@Override
		protected Task createFlowElement(ICreateContext context) {
			BusinessRuleTask task = ModelHandler.FACTORY.createBusinessRuleTask();
			task.setName("Business Rule Task");
			task.setImplementation("##unspecified");
			return task;
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_BUSINESS_RULE_TASK;
		}
	}
}