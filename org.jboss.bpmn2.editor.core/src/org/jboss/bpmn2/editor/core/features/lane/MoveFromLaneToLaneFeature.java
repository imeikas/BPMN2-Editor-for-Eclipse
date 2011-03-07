package org.jboss.bpmn2.editor.core.features.lane;

import java.io.IOException;

import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Participant;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.ITargetContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.utils.FeatureSupport;

public class MoveFromLaneToLaneFeature extends MoveLaneFeature {

	public MoveFromLaneToLaneFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		Lane targetLane = getTargetLane(context);

		if (targetLane.getFlowNodeRefs() != null && targetLane.getFlowNodeRefs().size() > 0) {
			return false;
		}

		Lane movedLane = getMovedLane(context);

		if (targetLane.getChildLaneSet() != null && targetLane.getChildLaneSet().getLanes().contains(movedLane)) {
			return false;
		}

		return true;
	}

	@Override
	protected void internalMove(IMoveShapeContext context) {
		Lane movedLane = getMovedLane(context);
		Lane targetLane = getTargetLane(context);
		Lane sourceLane = getSourceLane(context);

		modifyModelStructure(sourceLane, targetLane, movedLane);

		FeatureSupport.redraw(context.getSourceContainer());
		FeatureSupport.redraw(context.getTargetContainer());
	}

	private void modifyModelStructure(Lane sourceLane, Lane targetLane, Lane movedLane) {
		if (targetLane.getChildLaneSet() == null) {
			LaneSet createLaneSet = ModelHandler.FACTORY.createLaneSet();
			createLaneSet.setId(EcoreUtil.generateUUID());
			targetLane.setChildLaneSet(createLaneSet);
		}

		try {
			ModelHandler handler = FeatureSupport.getModelHanderInstance(getDiagram());
			Participant sourceParticipant = handler.getParticipant(sourceLane);
			Participant targetParticipant = handler.getParticipant(targetLane);
			if (!sourceParticipant.equals(targetParticipant)) {
				handler.moveLane(movedLane, sourceParticipant, targetParticipant);
			}
		} catch (IOException e) {
			Activator.logError(e);
		}

		targetLane.getChildLaneSet().getLanes().add(movedLane);
		sourceLane.getChildLaneSet().getLanes().remove(movedLane);

		if (sourceLane.getChildLaneSet().getLanes().isEmpty()) {
			sourceLane.setChildLaneSet(null);
		}
	}

	private Lane getTargetLane(ITargetContext context) {
		ContainerShape targetContainer = context.getTargetContainer();
		return (Lane) getBusinessObjectForPictogramElement(targetContainer);
	}

	private Lane getSourceLane(IMoveShapeContext context) {
		ContainerShape sourceContainer = context.getSourceContainer();
		return (Lane) getBusinessObjectForPictogramElement(sourceContainer);
	}
}
