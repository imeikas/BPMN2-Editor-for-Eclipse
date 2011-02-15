package org.jboss.bpmn2.editor.core.features.gateway;

import org.eclipse.bpmn2.InclusiveGateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateInclusiveGatewayFeature extends AbstractCreateFlowElementFeature<InclusiveGateway> {
	
	public CreateInclusiveGatewayFeature(IFeatureProvider fp) {
	    super(fp, "Inclusive Gateway", "Used for creating alternative but also parallel paths");
    }

	@Override
    protected InclusiveGateway createFlowElement(ICreateContext context) {
		return ModelHandler.FACTORY.createInclusiveGateway();
    }
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_INCLUSIVE_GATEWAY;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); // FIXME
	}
}
