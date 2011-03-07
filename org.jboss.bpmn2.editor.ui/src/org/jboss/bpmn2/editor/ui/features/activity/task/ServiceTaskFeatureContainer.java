package org.jboss.bpmn2.editor.ui.features.activity.task;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.ServiceTask;
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
import org.jboss.bpmn2.editor.ui.ImageProvider;

public class ServiceTaskFeatureContainer extends AbstractTaskFeatureContainer {

	@Override
    public boolean canApplyTo(BaseElement element) {
	    return element instanceof ServiceTask;
    }

	@Override
    public ICreateFeature getCreateFeature(IFeatureProvider fp) {
	    return new CreateServiceTaskFeature(fp);
    }

	@Override
    public IAddFeature getAddFeature(IFeatureProvider fp) {
	    return new AddTaskFeature(fp){
	    	@Override
	    	protected void decorateActivityRectangle(RoundedRectangle rect) {
	    		IGaService service = Graphiti.getGaService();
	    		Image img = service.createImage(rect, ImageProvider.IMG_16_SERVICE_TASK);
	    		service.setLocationAndSize(img, 2, 2, 16, 16);
	    	}
	    };
    }
	
	public static class CreateServiceTaskFeature extends AbstractCreateTaskFeature {

		public CreateServiceTaskFeature(IFeatureProvider fp) {
			super(fp, "Service Task", "Task that uses some kind of service");
		}

		@Override
		protected Task createFlowElement(ICreateContext context) {
			ServiceTask task = ModelHandler.FACTORY.createServiceTask();
		    task.setName("Service Task");
		    task.setImplementation("##unspecified");
		    return task;
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_SERVICE_TASK;
		}
	}
}
