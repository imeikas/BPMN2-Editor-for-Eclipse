package org.jboss.bpmn2.editor.core.features;

import java.util.List;

import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;

public class MoveFlowNodeFeature extends DefaultMoveShapeFeature {

	public MoveFlowNodeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		boolean canMove = super.canMoveShape(context);
		
		if(!canMove && isTargetLane(context)) {
			canMove = true;
		}
		
		return canMove;
	}

	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		super.postMoveShape(context);
		List<Lane> lanes = getFlowNode(context).getLanes();
		
		if(isTargetLane(context)) {
			Lane targetLane = (Lane) getBusinessObjectForPictogramElement(context.getTargetContainer());
			lanes.add(targetLane);
		}
		
		if(isSourceLane(context)) {
			Lane sourceLane = (Lane) getBusinessObjectForPictogramElement(context.getSourceContainer());
			lanes.remove(sourceLane);
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
	
	private FlowNode getFlowNode(IMoveShapeContext context) {
		return (FlowNode) getBusinessObjectForPictogramElement(context.getShape());
	}
}
