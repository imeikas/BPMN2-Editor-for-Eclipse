package org.jboss.bpmn2.editor.core.features.gateway;

import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class AddParallelGatewayFeature extends AbstractAddGatewayFeature<ParallelGateway> {

	public AddParallelGatewayFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    protected Class<? extends Gateway> getGatewayClass() {
	    return ParallelGateway.class;
    }
	
	@Override
	protected void decorateGateway(Polygon diamond) {
		IGaService gaService = Graphiti.getGaService();
		
		Polyline verticalLine = gaService.createPolyline(diamond, new int [] {24, 7, 24, 43});
		verticalLine.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		verticalLine.setLineWidth(3);
		
		Polyline horizontalLine = gaService.createPolyline(diamond, new int [] {7, 24, 43, 24});
		horizontalLine.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		horizontalLine.setLineWidth(3);
	}
}