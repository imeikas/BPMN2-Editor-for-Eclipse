package org.jboss.bpmn2.editor.core.features.gateway;

import org.eclipse.bpmn2.EventBasedGateway;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class AddEventBasedGatewayFeature extends AbstractAddGatewayFeature<EventBasedGateway> {

	public AddEventBasedGatewayFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    protected Class<? extends Gateway> getGatewayClass() {
	    return EventBasedGateway.class;
    }
	
	@Override
	protected void decorateGateway(Polygon diamond) {
		decorateEllipse(ShapeUtil.createOuterCircle(diamond));
		decorateEllipse(ShapeUtil.createInnerCircle(diamond));
		Polygon pentagon = ShapeUtil.createPentagon(diamond);
		pentagon.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		pentagon.setFilled(false);
	}
	
	private void decorateEllipse(Ellipse ellipse) {
		ellipse.setFilled(false);
		ellipse.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		ellipse.setLineWidth(1);
	}
}
