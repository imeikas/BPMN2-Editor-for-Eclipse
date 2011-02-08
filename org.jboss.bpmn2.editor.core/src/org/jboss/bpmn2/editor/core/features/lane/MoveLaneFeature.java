package org.jboss.bpmn2.editor.core.features.lane;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public class MoveLaneFeature extends DefaultMoveShapeFeature {

	private enum MoveMode {
		DIAGRAM_TO_LANE, LANE_TO_DIAGRAM, LANE_TO_LANE, OTHER
	}

	protected FeatureSupport support = new FeatureSupport() {
		@Override
		protected Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};

	private MoveLaneFeature moveStrategy;

	public MoveLaneFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		if (context.getSourceContainer() == null) {
			return false;
		}

		MoveMode mode = getMoveMode(context);

		switch (mode) {
		case DIAGRAM_TO_LANE:
			moveStrategy = new MoveFromDiagramFeature(getFeatureProvider());
			break;
		case LANE_TO_DIAGRAM:
			moveStrategy = new MoveFromLaneFeature(getFeatureProvider());
			break;
		case LANE_TO_LANE:
			moveStrategy = new MoveFromLaneToLaneFeature(getFeatureProvider());
			break;
		default:
			break;
		}
		
		if (mode == MoveMode.OTHER) {
			return super.canMoveShape(context);
		}

		return moveStrategy.canMoveShape(context);
	}

	@Override
	protected void internalMove(IMoveShapeContext context) {
		super.internalMove(context);
		if (moveStrategy != null) {
			moveStrategy.internalMove(context);
		}
	}

	private MoveMode getMoveMode(IMoveShapeContext context) {
		if (context.getSourceContainer().equals(getDiagram()) && support.isTargetLane(context)) {
			return MoveMode.DIAGRAM_TO_LANE;
		}
		if (support.isLane(context.getSourceContainer()) && context.getTargetContainer().equals(getDiagram())) {
			return MoveMode.LANE_TO_DIAGRAM;
		}
		if (support.isLane(context.getSourceContainer()) && support.isTargetLane(context)) {
			return MoveMode.LANE_TO_LANE;
		}
		return MoveMode.OTHER;
	}
}