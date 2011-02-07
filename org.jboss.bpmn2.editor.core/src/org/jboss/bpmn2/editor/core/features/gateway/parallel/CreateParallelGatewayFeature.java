package org.jboss.bpmn2.editor.core.features.gateway.parallel;

import java.io.IOException;

import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public class CreateParallelGatewayFeature extends AbstractCreateFeature {

	private FeatureSupport support = new FeatureSupport() {
		@Override
		protected Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};
	
	public CreateParallelGatewayFeature(IFeatureProvider fp) {
	    super(fp, "Parallel Gateway", "Used to combine or create parallel flows");
    }
	
	@Override
    public boolean canCreate(ICreateContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = support.isTargetLane(context) && support.isTargetLaneOnTop(context); 
		return intoDiagram || intoLane;
    }

	@Override
    public Object[] create(ICreateContext context) {
		try {
			ModelHandler mh = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
			ParallelGateway gateway = mh.addFlowElement(ModelHandler.FACTORY.createParallelGateway());
			if(support.isTargetLane(context)) {
				gateway.getLanes().add((Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
			}
			addGraphicalRepresentation(context, gateway);
			return new Object[] { gateway };
		} catch (IOException e) {
			Activator.logError(e);
		}
		return null;
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
