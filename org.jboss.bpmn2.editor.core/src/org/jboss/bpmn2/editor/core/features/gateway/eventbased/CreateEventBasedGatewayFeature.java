package org.jboss.bpmn2.editor.core.features.gateway.eventbased;

import org.eclipse.bpmn2.EventBasedGateway;
import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateEventBasedGatewayFeature extends AbstractCreateFlowElementFeature<EventBasedGateway> {

	public CreateEventBasedGatewayFeature(IFeatureProvider fp) {
	    super(fp, "Event-Based Gateway", "Represents a branching point in the process");
    }
	
	@Override
    protected EventBasedGateway createFlowElement(ICreateContext context) {
		EventBasedGateway gateway = ModelHandler.FACTORY.createEventBasedGateway();
		if(support.isTargetLane(context)) {
			gateway.getLanes().add((Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
		}
	    return gateway;
    }
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_EVENT_BASED_GATEWAY;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); // FIXME
	}
}
