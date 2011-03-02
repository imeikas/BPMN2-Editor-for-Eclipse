package org.jboss.bpmn2.editor.core.features.lane;

import java.io.IOException;

import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public class CreateLaneFeature extends AbstractCreateFeature {

	private static int index = 1;

	public CreateLaneFeature(IFeatureProvider fp) {
		super(fp, "Lane", "A sub-partition in a process that helps to organize and categorize activities");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context);
		boolean intoParticipant = FeatureSupport.isTargetParticipant(context);
		boolean intoSubProcess = FeatureSupport.isTargetSubProcess(context);
		return intoDiagram || intoLane || intoParticipant || intoSubProcess;
	}

	@Override
	public Object[] create(ICreateContext context) {
		Lane lane = null;
		try {
			ModelHandler mh = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
			Object o = getBusinessObjectForPictogramElement(context.getTargetContainer());
			if (FeatureSupport.isTargetLane(context)) {
				Lane targetLane = (Lane) o;
				lane = mh.createLane(targetLane);
			} else {
				lane = mh.createLane(o);
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