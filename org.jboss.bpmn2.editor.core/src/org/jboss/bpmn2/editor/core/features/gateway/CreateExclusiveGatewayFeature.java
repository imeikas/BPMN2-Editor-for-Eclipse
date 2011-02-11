package org.jboss.bpmn2.editor.core.features.gateway;

import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateExclusiveGatewayFeature extends AbstractCreateFlowElementFeature<ExclusiveGateway> {
	
	public CreateExclusiveGatewayFeature(IFeatureProvider fp) {
		super(fp, "Exclusive Gateway", "Exclusive decision and merging");
	}

	@Override
    protected ExclusiveGateway createFlowElement(ICreateContext context) {
		ExclusiveGateway gateway = ModelHandler.FACTORY.createExclusiveGateway();
		if(support.isTargetLane(context)) {
			gateway.getLanes().add((Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
		}
		return gateway;
    }
	
	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_EXCLUSIVE_GATEWAY;
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); // FIXME
	}
}
