package org.jboss.bpmn2.editor.core.features.lane;

import java.io.IOException;

import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Participant;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class MoveFromParticipantToLaneFeature extends MoveLaneFeature {

	public MoveFromParticipantToLaneFeature(IFeatureProvider fp) {
	    super(fp);
    }
	
	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		Lane movedLane = getMovedLane(context);
		boolean moveableHasFlowNodes = movedLane.getFlowNodeRefs().size() > 0;

		Lane targetLane = getTargetLane(context);
		boolean targetHasFlowNodeRefs = targetLane.getFlowNodeRefs().size() > 0;

		if (!moveableHasFlowNodes && !targetHasFlowNodeRefs)
			return true;

		return moveableHasFlowNodes ^ targetHasFlowNodeRefs;
	}
	
	@Override
	protected void internalMove(IMoveShapeContext context) {
		modifyModelStructure(context);
		support.redraw(context.getSourceContainer());
		support.redraw(context.getTargetContainer());
	}
	
	private Lane getTargetLane(IMoveShapeContext context) {
		ContainerShape targetContainer = context.getTargetContainer();
		return (Lane) getBusinessObjectForPictogramElement(targetContainer);
	}
	
	private void modifyModelStructure(IMoveShapeContext context) {
		Lane movedLane = getMovedLane(context);
		Lane toLane = getTargetLane(context);
		
		try {
			ModelHandler handler = support.getModelHanderInstance(getDiagram());
			Participant participant = handler.getParticipant(toLane);
			handler.moveLane(movedLane, participant);
		} catch (IOException e) {
			Activator.logError(e);
		}
		
		Participant sourceParticipant = (Participant) getBusinessObjectForPictogramElement(context.getSourceContainer());
		
		LaneSet laneSet = null;
		for(LaneSet set : sourceParticipant.getProcessRef().getLaneSets()) {
			if(set.getLanes().contains(movedLane)) {
				laneSet = set;
				break;
			}
		}
		
		if(laneSet != null) {
			laneSet.getLanes().remove(movedLane);
			if(laneSet.getLanes().isEmpty()) {
				sourceParticipant.getProcessRef().getLaneSets().remove(laneSet);
			}
		}
		
		if(toLane.getChildLaneSet() == null) {
			toLane.setChildLaneSet(ModelHandler.FACTORY.createLaneSet());
		}
		toLane.getChildLaneSet().getLanes().addAll(laneSet.getLanes());
	}
}