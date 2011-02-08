package org.jboss.bpmn2.editor.core.features.gateway.exclusive;

import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.jboss.bpmn2.editor.core.features.gateway.AbstractAddGatewayFeature;

public class AddExclusiveGatewayFeature extends AbstractAddGatewayFeature {

	public AddExclusiveGatewayFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
    protected Class<? extends Gateway> getGatewayClass() {
	    return ExclusiveGateway.class;
    }
}