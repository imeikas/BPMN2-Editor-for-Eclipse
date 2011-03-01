package org.jboss.bpmn2.editor.core.features.lane;

import java.io.IOException;

import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class MoveFromParticipantToParticipantFeature extends MoveLaneFeature {

	public MoveFromParticipantToParticipantFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		Participant p1 = (Participant) BusinessObjectUtil.getFirstElementOfType(context.getSourceContainer(),
				Participant.class);
		Participant p2 = (Participant) BusinessObjectUtil.getFirstElementOfType(context.getTargetContainer(),
				Participant.class);

		if (p1.equals(p2)) {
			return false;
		}

		if (getMovedLane(context).getFlowNodeRefs().isEmpty()) {
			return true;
		}

		if (p2.getProcessRef() == null) {
			return true;
		}

		if (!p2.getProcessRef().getLaneSets().isEmpty()) {
			return true;
		}

		return false;
	}

	@Override
	protected void internalMove(IMoveShapeContext context) {
		modifyModelStructure(context);
		support.redraw(context.getSourceContainer());
		support.redraw(context.getTargetContainer());
	}

	private void modifyModelStructure(IMoveShapeContext context) {
		Lane movedLane = getMovedLane(context);
		Participant targetParticipant = (Participant) BusinessObjectUtil.getFirstElementOfType(
				context.getTargetContainer(), Participant.class);

		try {
			ModelHandler handler = support.getModelHanderInstance(getDiagram());
			handler.moveLane(movedLane, targetParticipant);
		} catch (IOException e) {
			Activator.logError(e);
		}

		Participant sourceParticipant = (Participant) BusinessObjectUtil.getFirstElementOfType(
				context.getSourceContainer(), Participant.class);

		LaneSet laneSet = null;
		for (LaneSet set : sourceParticipant.getProcessRef().getLaneSets()) {
			if (set.getLanes().contains(movedLane)) {
				laneSet = set;
				break;
			}
		}

		if (laneSet != null) {
			laneSet.getLanes().remove(movedLane);
			if (laneSet.getLanes().isEmpty()) {
				sourceParticipant.getProcessRef().getLaneSets().remove(laneSet);
			}

			Process process = targetParticipant.getProcessRef();
			if (process.getLaneSets().isEmpty()) {
				process.getLaneSets().add(ModelHandler.FACTORY.createLaneSet());
			}
			process.getLaneSets().get(0).getLanes().add(movedLane);
		}
	}
}