package org.jboss.bpmn2.editor.core.features.gateway;

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
    protected ParallelGateway createFlowElement(ICreateContext context) {
		return ModelHandler.FACTORY.createParallelGateway();
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
