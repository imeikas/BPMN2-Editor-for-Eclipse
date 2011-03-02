package org.jboss.bpmn2.editor.core.features.lane;

import java.io.IOException;

import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class MoveFromParticipantToDiagramFeature extends MoveLaneFeature {

	public MoveFromParticipantToDiagramFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		return true;
	}

	@Override
	protected void internalMove(IMoveShapeContext context) {
		modifyModelStructure(context);
		support.redraw(context.getSourceContainer());
	}

	private void modifyModelStructure(IMoveShapeContext context) {
		Lane movedLane = getMovedLane(context);
		Participant sourceParticipant = (Participant) getBusinessObjectForPictogramElement(context.getSourceContainer());
		Participant internalParticipant = null;

		try {
			ModelHandler handler = support.getModelHanderInstance(getDiagram());
			internalParticipant = handler.getInternalParticipant();
			handler.moveLane(movedLane, internalParticipant);
		} catch (IOException e) {
			Activator.logError(e);
		}

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

			Process process = internalParticipant.getProcessRef();
			if (process.getLaneSets().isEmpty()) {
				LaneSet createLaneSet = ModelHandler.FACTORY.createLaneSet();
				createLaneSet.setId(EcoreUtil.generateUUID());
				process.getLaneSets().add(createLaneSet);
			}
			process.getLaneSets().get(0).getLanes().add(movedLane);
		}
	}
}
