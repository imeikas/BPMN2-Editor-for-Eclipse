package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.utils.ShapeUtil;
import org.jboss.bpmn2.editor.utils.StyleUtil;

public class IntermediateCatchEventFeatureContainer extends AbstractEventFeatureContainer {

	@Override
    public boolean canApplyTo(BaseElement element) {
	    return element instanceof IntermediateCatchEvent;
    }

	@Override
    public ICreateFeature getCreateFeature(IFeatureProvider fp) {
	    return new CreateIntermediateCatchEventFeature(fp);
    }

	@Override
    public IAddFeature getAddFeature(IFeatureProvider fp) {
	    return new AddEventFeature(fp){
	    	@Override
	    	protected void decorateEllipse(Ellipse e) {
	    		Ellipse circle = ShapeUtil.createIntermediateEventCircle(e);
	    		circle.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
	    	}
	    };
    }
	
	public static class CreateIntermediateCatchEventFeature extends AbstractCreateEventFeature {

		public CreateIntermediateCatchEventFeature(IFeatureProvider fp) {
			super(fp, "Catch Event", "Token remains at the event until event trigger will occur");
		}

		@Override
		protected IntermediateCatchEvent createFlowElement(ICreateContext context) {
			IntermediateCatchEvent event = ModelHandler.FACTORY.createIntermediateCatchEvent();
			event.setName("Catch");
			return event;
		}
		
		@Override
        String getStencilImageId() {
			return ImageProvider.IMG_16_INTERMEDIATE_CATCH_EVENT;
        }
	}
}