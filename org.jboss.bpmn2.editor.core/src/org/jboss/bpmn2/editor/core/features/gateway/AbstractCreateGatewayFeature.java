package org.jboss.bpmn2.editor.core.features.gateway;

import org.eclipse.bpmn2.Gateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public abstract class AbstractCreateGatewayFeature extends AbstractCreateFlowElementFeature<Gateway> {

	public AbstractCreateGatewayFeature(IFeatureProvider fp, String name, String description) {
	    super(fp, name, description);
    }

	protected abstract String getStencilImageId();
	
	@Override
	public String getCreateImageId() {
	    return getStencilImageId();
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId();
	}
}
