package org.jboss.bpmn2.editor.core.features.lane;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public class MoveLaneFeature extends DefaultMoveShapeFeature {

	protected FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return BusinessObjectUtil.getFirstElementOfType(element, BaseElement.class);
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

		moveStrategy = getStrategy(context);

		if (moveStrategy == null) {
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

	private MoveLaneFeature getStrategy(IMoveShapeContext context) {

		if (context.getSourceContainer().equals(getDiagram())) { // from diagram

			if (support.isTargetLane(context)) { // to lane
				return new MoveFromDiagramToLaneFeature(getFeatureProvider());
			} else if (support.isTargetParticipant(context)) { // to participant
				return new MoveFromDiagramToParticipantFeature(getFeatureProvider());
			}

		} else if (support.isLane(context.getSourceContainer())) { // from lane

			if (context.getTargetContainer().equals(getDiagram())) { // to diagram
				return new MoveFromLaneToDiagramFeature(getFeatureProvider());
			} else if (support.isTargetLane(context)) { // to another lane
				return new MoveFromLaneToLaneFeature(getFeatureProvider());
			} else if (support.isTargetParticipant(context)) { // to participant
				return new MoveFromLaneToParticipantFeature(getFeatureProvider());
			}

		} else if (support.isParticipant(context.getSourceContainer())) { // from participant

			if (context.getTargetContainer().equals(getDiagram())) { // to diagram
				return new MoveFromParticipantToDiagramFeature(getFeatureProvider());
			} else if (support.isTargetLane(context)) { // to another lane
				return new MoveFromParticipantToLaneFeature(getFeatureProvider());
			} else if (support.isTargetParticipant(context)) { // to another participant
				return new MoveFromParticipantToParticipantFeature(getFeatureProvider());
			}
		}

		return null;
	}

	protected Lane getMovedLane(IMoveShapeContext context) {
		return (Lane) BusinessObjectUtil.getFirstElementOfType(context.getShape(), Lane.class);
	}
}