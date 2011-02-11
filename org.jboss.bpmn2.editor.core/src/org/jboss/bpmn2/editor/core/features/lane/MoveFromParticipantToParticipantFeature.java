package org.jboss.bpmn2.editor.core.features.lane;

import org.eclipse.bpmn2.Participant;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;

public class MoveFromParticipantToParticipantFeature extends MoveLaneFeature {

	public MoveFromParticipantToParticipantFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		Participant p1 = (Participant) getBusinessObjectForPictogramElement(context.getSourceContainer());
		Participant p2 = (Participant) getBusinessObjectForPictogramElement(context.getTargetContainer());

		if (p1.equals(p2))
			return false;

		if (getMovedLane(context).getFlowNodeRefs().isEmpty())
			return true;

		if (p2.getProcessRef() == null)
			return true;
		
		if (!p2.getProcessRef().getLaneSets().isEmpty())
			return true;
		
		return false;
	}
}