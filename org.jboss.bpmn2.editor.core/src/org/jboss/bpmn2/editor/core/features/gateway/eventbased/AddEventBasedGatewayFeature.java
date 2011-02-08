package org.jboss.bpmn2.editor.core.features.gateway.eventbased;

import org.eclipse.bpmn2.EventBasedGateway;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.features.StyleUtil;
import org.jboss.bpmn2.editor.core.features.gateway.AbstractAddGatewayFeature;

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
		createEllipse(diamond, 12, 12, 27, 27);
		createEllipse(diamond, 14, 14, 23, 23);
		IGaService gaService = Graphiti.getGaService();
		Polygon polygon = gaService.createPolygon(diamond, new int[] {RADIUS, 18, RADIUS + 8, RADIUS - 2, RADIUS + 5, RADIUS + 7, RADIUS - 5, RADIUS + 7, RADIUS - 8, RADIUS - 2});
		polygon.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		polygon.setFilled(false);
	}
	
	private Ellipse createEllipse(Polygon diamond, int x, int y, int w, int h) {
		IGaService gaService = Graphiti.getGaService();
		Ellipse ellipse = gaService.createEllipse(diamond);
		ellipse.setFilled(false);
		ellipse.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		ellipse.setLineWidth(1);
		gaService.setLocationAndSize(ellipse, x, y, w, h);
		return ellipse;
	}
}
