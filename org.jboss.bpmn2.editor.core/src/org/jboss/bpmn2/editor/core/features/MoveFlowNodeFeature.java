package org.jboss.bpmn2.editor.core.features;

import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;

public class MoveFlowNodeFeature extends DefaultMoveShapeFeature {

	private enum MoveMode {
		DIAGRAM_TO_LANE, LANE_TO_DIAGRAM, LANE_TO_LANE, OTHER
	}

	private MoveMode moveMode;

	public MoveFlowNodeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		moveMode = MoveMode.OTHER;
		boolean fromDiagram = context.getSourceContainer().equals(getDiagram());
		boolean toDiagram = context.getTargetContainer().equals(getDiagram());
		boolean fromLane = isSourceLane(context);
		boolean toLane = isTargetLane(context);

		if (fromDiagram && toLane && isTargetLaneOnTop(context)) {
			moveMode = MoveMode.DIAGRAM_TO_LANE;
		} else if (fromLane && toDiagram) {
			moveMode = MoveMode.LANE_TO_DIAGRAM;
		} else if (fromLane && toLane && isTargetLaneOnTop(context)) {
			moveMode = MoveMode.LANE_TO_LANE;
		}

		if (moveMode == MoveMode.OTHER) {
			return super.canMoveShape(context);
		}

		return true;
	}

	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		switch (moveMode) {
		case DIAGRAM_TO_LANE:
			moveFromDiagramToLane(context);
			break;
		case LANE_TO_DIAGRAM:
			moveFromLaneToDiagram(context);
			break;
		case LANE_TO_LANE:
			moveFromLaneToLane(context);
			break;
		default:
			super.internalMove(context);
			break;
		}
	}

	private boolean isTargetLane(IMoveShapeContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
		return bo != null && bo instanceof Lane;
	}

	private boolean isSourceLane(IMoveShapeContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getSourceContainer());
		return bo != null && bo instanceof Lane;
	}
	
	private boolean isTargetLaneOnTop(IMoveShapeContext context) {
		Lane lane = (Lane) getBusinessObjectForPictogramElement(context.getTargetContainer());
		return lane.getChildLaneSet() == null || lane.getChildLaneSet().getLanes().isEmpty();
	}
	
	private void moveFromDiagramToLane(IMoveShapeContext context) {
		FlowNode node = (FlowNode) getBusinessObjectForPictogramElement(context.getShape());
		node.getLanes().add((Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
	}
	
	private void moveFromLaneToDiagram(IMoveShapeContext context) {
		FlowNode node = (FlowNode) getBusinessObjectForPictogramElement(context.getShape());
		node.getLanes().remove((Lane) getBusinessObjectForPictogramElement(context.getSourceContainer()));
	}

	private void moveFromLaneToLane(IMoveShapeContext context) {
		FlowNode node = (FlowNode) getBusinessObjectForPictogramElement(context.getShape());
		node.getLanes().remove((Lane) getBusinessObjectForPictogramElement(context.getSourceContainer()));
		node.getLanes().add((Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
	}
}
