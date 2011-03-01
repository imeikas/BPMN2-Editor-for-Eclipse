package org.jboss.bpmn2.editor.core.features.activity.task;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.ManualTask;
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

public class ManualTaskFeatureContainer extends AbstractTaskFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof ManualTask;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateManualTaskFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddTaskFeature(fp) {
			@Override
			protected void decorateActivityRectangle(RoundedRectangle rect) {
				IGaService service = Graphiti.getGaService();
				Image img = service.createImage(rect, ImageProvider.IMG_16_MANUAL_TASK);
				service.setLocationAndSize(img, 0, 0, 16, 16);
			}
		};
	}

	public static class CreateManualTaskFeature extends AbstractCreateTaskFeature {

		public CreateManualTaskFeature(IFeatureProvider fp) {
			super(fp, "Manual Task",
	        "Task that is expected to perform without the aid of any business process execution engine or any application");
		}

		@Override
		protected Task createFlowElement(ICreateContext context) {
			ManualTask task = ModelHandler.FACTORY.createManualTask();
			task.setName("Manual Task");
			return task;
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_MANUAL_TASK;
		}
	}
}