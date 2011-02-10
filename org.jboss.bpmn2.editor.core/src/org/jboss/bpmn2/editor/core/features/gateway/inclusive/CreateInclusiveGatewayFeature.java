package org.jboss.bpmn2.editor.core.features.gateway.inclusive;

import org.eclipse.bpmn2.InclusiveGateway;
import org.eclipse.bpmn2.Lane;
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
		InclusiveGateway gateway = ModelHandler.FACTORY.createInclusiveGateway();
		if(support.isTargetLane(context)) {
			gateway.getLanes().add((Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
		}
		return gateway;
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
