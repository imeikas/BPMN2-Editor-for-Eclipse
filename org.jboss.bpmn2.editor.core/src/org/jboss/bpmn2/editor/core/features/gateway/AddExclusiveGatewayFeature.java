package org.jboss.bpmn2.editor.core.features.gateway;

import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;

public class AddExclusiveGatewayFeature extends AbstractAddGatewayFeature<ExclusiveGateway> {

	public AddExclusiveGatewayFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
    protected Class<? extends Gateway> getGatewayClass() {
	    return ExclusiveGateway.class;
    }
	
}