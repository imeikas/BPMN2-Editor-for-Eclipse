package org.jboss.bpmn2.editor.core.features.lane;

import java.io.IOException;

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

public class CreateLaneFeature extends AbstractCreateFeature {
	
	private static int index = 1;
	
	private FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};

	public CreateLaneFeature(IFeatureProvider fp) {
		super(fp, "Lane", "A sub-partition in a process that helps to organize and categorize activities");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = support.isTargetLane(context);
		boolean intoParticipant = support.isTargetParticipant(context);
		return intoDiagram || intoLane || intoParticipant;
	}

	@Override
	public Object[] create(ICreateContext context) {
		Lane lane = null;
		try {
			ModelHandler mh = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
			if (support.isTargetLane(context)) {
				Lane targetLane = (Lane) getBusinessObjectForPictogramElement(context.getTargetContainer());
				lane = mh.addLaneTo(targetLane);
			} else if (support.isTargetParticipant(context)) {
				lane = mh.addLane(support.getTargetParticipant(context, mh));
			} else {
				lane = mh.addLane(mh.getInternalParticipant());
			}
			lane.setName("Lane nr " + index++);
		} catch (IOException e) {
			Activator.logError(e);
		}
		addGraphicalRepresentation(context, lane);
		return new Object[] { lane };
	}

	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_LANE;
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); // FIXME
	}
}
