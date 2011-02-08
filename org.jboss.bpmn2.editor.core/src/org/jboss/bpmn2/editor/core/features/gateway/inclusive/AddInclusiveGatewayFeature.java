package org.jboss.bpmn2.editor.core.features.gateway.inclusive;

import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.InclusiveGateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.features.StyleUtil;
import org.jboss.bpmn2.editor.core.features.gateway.AbstractAddGatewayFeature;

public class AddInclusiveGatewayFeature extends AbstractAddGatewayFeature<InclusiveGateway> {
	
	public AddInclusiveGatewayFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    protected Class<? extends Gateway> getGatewayClass() {
	    return InclusiveGateway.class;
    }
	
	@Override
	protected void decorateGateway(Polygon diamond) {
		IGaService gaService = Graphiti.getGaService();
		Ellipse ellipse = gaService.createEllipse(diamond);
		ellipse.setFilled(false);
		ellipse.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		ellipse.setLineWidth(2);
		gaService.setLocationAndSize(ellipse, 12, 12, 27, 27);
	}
}
