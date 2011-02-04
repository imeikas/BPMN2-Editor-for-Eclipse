package org.jboss.bpmn2.editor.core.features;

import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;

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
		if(isTargetLane(context)) {
			Lane lane = (Lane) getTargetBusinessObj(context);
			getSourceBusinessObj(context).getLanes().add(lane);
		}
	}
	
	private boolean isTargetLane(IMoveShapeContext context) {
		Object targetBusinessObj = getTargetBusinessObj(context);
		return targetBusinessObj != null && targetBusinessObj instanceof Lane;
	}
	
	private Object getTargetBusinessObj(IMoveShapeContext context) {
		PictogramLink link = context.getTargetContainer().getLink();
		if(link == null) {
			return null;
		}
		return link.getBusinessObjects().get(0);
	}
	
	private FlowNode getSourceBusinessObj(IMoveShapeContext context) {
		return (FlowNode) getBusinessObjectForPictogramElement(context.getShape());
	}
}
