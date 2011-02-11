package org.jboss.bpmn2.editor.core.features.lane;

import org.eclipse.bpmn2.Participant;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;

public class MoveFromDiagramToParticipantFeature extends MoveLaneFeature {

	public MoveFromDiagramToParticipantFeature(IFeatureProvider fp) {
	    super(fp);
    }
	
	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		Participant p = (Participant) getBusinessObjectForPictogramElement(context.getTargetContainer());

		if (getMovedLane(context).getFlowNodeRefs().isEmpty())
			return true;

		if (p.getProcessRef() == null)
			return true;
		
		if (!p.getProcessRef().getLaneSets().isEmpty())
			return true;
		
		return false;
	}
}
