package org.jboss.bpmn2.editor.core.features.lane;

import org.eclipse.bpmn2.Participant;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;

public class MoveFromLaneToParticipantFeature extends MoveLaneFeature {

	public MoveFromLaneToParticipantFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		if (getMovedLane(context).getFlowNodeRefs().isEmpty())
			return true;

		Participant p = (Participant) getBusinessObjectForPictogramElement(context.getTargetContainer());

		if (p.getProcessRef() == null)
			return true;

		if (!p.getProcessRef().getLaneSets().isEmpty())
			return true;

		return false;
	}
}
