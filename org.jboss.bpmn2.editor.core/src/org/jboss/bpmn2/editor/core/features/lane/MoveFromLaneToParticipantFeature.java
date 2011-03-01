package org.jboss.bpmn2.editor.core.features.lane;

import java.io.IOException;

import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class MoveFromLaneToParticipantFeature extends MoveLaneFeature {

	public MoveFromLaneToParticipantFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		if (getMovedLane(context).getFlowNodeRefs().isEmpty()) {
			return true;
		}

		Participant p = (Participant) BusinessObjectUtil.getFirstElementOfType(context.getTargetContainer(),
				Participant.class);

		if (p.getProcessRef() == null) {
			return true;
		}

		if (!p.getProcessRef().getLaneSets().isEmpty()) {
			return true;
		}

		return false;
	}

	@Override
	protected void internalMove(IMoveShapeContext context) {
		modifyModelStructure(context);
		support.redraw(context.getTargetContainer());
		support.redraw(context.getSourceContainer());
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

		Process process = targetParticipant.getProcessRef();
		if (process.getLaneSets().isEmpty()) {
			process.getLaneSets().add(ModelHandler.FACTORY.createLaneSet());
		}
		process.getLaneSets().get(0).getLanes().add(movedLane);

		Lane fromLane = (Lane) BusinessObjectUtil.getFirstElementOfType(context.getSourceContainer(), Lane.class);
		fromLane.getChildLaneSet().getLanes().remove(movedLane);
		if (fromLane.getChildLaneSet().getLanes().isEmpty()) {
			fromLane.setChildLaneSet(null);
		}
	}
}
