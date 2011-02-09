package org.jboss.bpmn2.editor.core.features.lane;

import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class MoveFromLaneToLaneFeature extends MoveLaneFeature {

	public MoveFromLaneToLaneFeature(IFeatureProvider fp) {
	    super(fp);
    }
	
	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		Lane targetLane = getTargetLane(context);
		
		if(targetLane.getFlowNodeRefs() != null && targetLane.getFlowNodeRefs().size() > 0) {
			return false;
		}
		
		Lane movedLane = getMovedLane(context);
		
		if(targetLane.getChildLaneSet() != null && targetLane.getChildLaneSet().getLanes().contains(movedLane)) {
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
		support.redraw(context.getSourceContainer());
	}
	
	private void modifyModelStructure(Lane sourceLane, Lane targetLane, Lane movedLane) {
		sourceLane.getChildLaneSet().getLanes().remove(movedLane);
		
		if(targetLane.getChildLaneSet() == null) {
			targetLane.setChildLaneSet(ModelHandler.FACTORY.createLaneSet());
		}
		targetLane.getChildLaneSet().getLanes().add(movedLane);
    }

	private Lane getTargetLane(IMoveShapeContext context) {
		ContainerShape targetContainer = context.getTargetContainer();
		return (Lane) getBusinessObjectForPictogramElement(targetContainer);
	}
	
	private Lane getMovedLane(IMoveShapeContext context) {
		return (Lane) getBusinessObjectForPictogramElement(context.getShape());
	}
	
	private Lane getSourceLane(IMoveShapeContext context) {
		ContainerShape sourceContainer = context.getSourceContainer();
		return (Lane) getBusinessObjectForPictogramElement(sourceContainer);
	}
}
