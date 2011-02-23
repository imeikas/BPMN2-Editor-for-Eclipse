package org.jboss.bpmn2.editor.core.features.gateway;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.EventBasedGateway;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class EventBasedGatewayFeatureContainer extends AbstractGatewayFeatureContainer {

	@Override
    public boolean canApplyTo(BaseElement element) {
	    return element instanceof EventBasedGateway;
    }

	@Override
    public ICreateFeature getCreateFeature(IFeatureProvider fp) {
	    return new CreateEventBasedGatewayFeature(fp);
    }

	@Override
    public IAddFeature getAddFeature(IFeatureProvider fp) {
	    return new DefaultAddGatewayFeature(fp){
	    	@Override
	    	protected void decorateGateway(Polygon gateway) {
	    		decorateEllipse(ShapeUtil.createOuterCircle(gateway));
	    		decorateEllipse(ShapeUtil.createInnerCircle(gateway));
	    		Polygon pentagon = ShapeUtil.createPentagon(gateway);
	    		pentagon.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
	    		pentagon.setFilled(false);
	    	}
	    	
	    	private void decorateEllipse(Ellipse ellipse) {
	    		ellipse.setFilled(false);
	    		ellipse.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
	    		ellipse.setLineWidth(1);
	    	}
	    };
    }
	
	public static class CreateEventBasedGatewayFeature extends AbstractCreateGatewayFeature {

		public CreateEventBasedGatewayFeature(IFeatureProvider fp) {
		    super(fp, "Event-Based Gateway", "Represents a branching point in the process");
	    }
		
		@Override
	    protected Gateway createFlowElement(ICreateContext context) {
			return ModelHandler.FACTORY.createEventBasedGateway();
	    }
		
		@Override
        protected String getStencilImageId() {
			return ImageProvider.IMG_16_EVENT_BASED_GATEWAY;
        }
	}
}
