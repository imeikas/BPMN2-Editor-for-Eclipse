package org.jboss.bpmn2.editor.core.features.event.end;

import java.io.IOException;

import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public class CreateEndEventFeature extends AbstractCreateFeature {
	
	private FeatureSupport support = new FeatureSupport() {
		@Override
		protected Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};
	
	public CreateEndEventFeature(IFeatureProvider fp) {
		super(fp, "End Event", "Indicates the end of a process or choreography");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = support.isTargetLane(context) && support.isTargetLaneOnTop(context); 
		return intoDiagram || intoLane;
	}

	@Override
	public Object[] create(ICreateContext context) {
		EndEvent end = null;
		try {
			ModelHandler mh = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
			end = mh.addFlowElement(ModelHandler.FACTORY.createEndEvent());
			end.setName("End");
			if(support.isTargetLane(context)) {
				end.getLanes().add((Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
			}
		} catch (IOException e) {
			Activator.logError(e);
		}

		addGraphicalRepresentation(context, end);

		return new Object[] { end };
	}

	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_END_EVENT;
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); // FIXME
	}
}
