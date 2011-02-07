package org.jboss.bpmn2.editor.core.features.lane;

import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;

public class MoveLaneFeature extends DefaultMoveShapeFeature {

	private MoveLaneFeature moveStrategy;

	public MoveLaneFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		if (context.getSourceContainer() == null) {
			return false;
		}
		
		if(isNormalMove(context)) {
			return super.canMoveShape(context);
		}
		
		moveStrategy = getMoveStrategy(context);
		return moveStrategy.canMoveShape(context);
	}

	@Override
	protected void internalMove(IMoveShapeContext context) {
		super.internalMove(context);
		if(moveStrategy != null) {
			moveStrategy.internalMove(context);
		}
	}

	private MoveLaneFeature getMoveStrategy(IMoveShapeContext context) {
		if(context.getSourceContainer().equals(getDiagram())) {
			return new MoveFromDiagramFeature(getFeatureProvider()); 
		} else if (isSourceLane(context)) {
			return new MoveFromLaneFeature(getFeatureProvider());
		} else {
			return null;
		}
	}
	
	private boolean isNormalMove(IMoveShapeContext context) {
		boolean fromDiagram = context.getSourceContainer().equals(getDiagram());
		boolean toDiagram = context.getTargetContainer().equals(getDiagram());
		return fromDiagram && toDiagram;
	}
	
	private boolean isSourceLane(IMoveShapeContext context) {
		return getBusinessObjectForPictogramElement(context.getSourceContainer()) instanceof Lane;
	}
}