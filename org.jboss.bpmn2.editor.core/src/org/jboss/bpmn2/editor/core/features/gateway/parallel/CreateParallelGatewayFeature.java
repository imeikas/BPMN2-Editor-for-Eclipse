package org.jboss.bpmn2.editor.core.features.gateway.parallel;

import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateParallelGatewayFeature extends AbstractCreateFlowElementFeature<ParallelGateway> {

	public CreateParallelGatewayFeature(IFeatureProvider fp) {
	    super(fp, "Parallel Gateway", "Used to combine or create parallel flows");
    }
	
	@Override
	protected ParallelGateway create(ICreateContext context, ModelHandler handler) {
		ParallelGateway gateway = handler.addFlowElement(ModelHandler.FACTORY.createParallelGateway());
		if(support.isTargetLane(context)) {
			gateway.getLanes().add((Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
		}
		return gateway;
	}
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_PARALLEL_GATEWAY;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); // FIXME
	}
}
