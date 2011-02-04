package org.jboss.bpmn2.editor.core.features.lane;

import java.util.List;

import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Process;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class MoveLaneFeature extends DefaultMoveShapeFeature {

	private MoveStrategy moveStrategy;

	public MoveLaneFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		if (context.getSourceContainer() == null) {
			return false;
		}
		moveStrategy = getMoveStrategy(context);
		return moveStrategy.canMoveShape(context);
	}

	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		super.postMoveShape(context);
		moveStrategy.postMoveShape(context);
	}

	private MoveStrategy getMoveStrategy(IMoveShapeContext context) {
		return context.getSourceContainer().equals(getDiagram()) ? new MoveFromDiagram() : new MoveFromLane();
	}

	private Object getTargetBusinessObj(IMoveShapeContext context) {
		PictogramLink link = context.getTargetContainer().getLink();
		if (link == null)
			return null;
		return link.getBusinessObjects().get(0);
	}

	private boolean isTargetLane(IMoveShapeContext context) {
		Object targetBusinessObj = getTargetBusinessObj(context);
		return targetBusinessObj != null && targetBusinessObj instanceof Lane;
	}

	private interface MoveStrategy {
		boolean canMoveShape(IMoveShapeContext context);

		void postMoveShape(IMoveShapeContext context);
	}

	private class MoveFromDiagram implements MoveStrategy {

		@Override
		public boolean canMoveShape(IMoveShapeContext context) {
			if (context.getSourceContainer().equals(context.getTargetContainer()))
				return true;

			if (isTargetLane(context))
				return true;

			return false;
		}

		@Override
		public void postMoveShape(IMoveShapeContext context) {
			if(!isTargetLane(context)) {
				return;
			}
			
			modifyStructure(context);
			
			IGaService gaService = Graphiti.getGaService();
			
			ContainerShape targetContainer = context.getTargetContainer();
			GraphicsAlgorithm targetGa = targetContainer.getGraphicsAlgorithm();
			IDimension targetSize = gaService.calculateSize(targetGa);
			
			((Lane) getTargetBusinessObj(context)).getChildLaneSet().getLanes().
		}
		
		private void modifyStructure(Lane targetLane, IMoveShapeContext context) {
			Lane lane = (Lane) getBusinessObjectForPictogramElement(context.getShape());
			
			LaneSet laneSet = (LaneSet) lane.eContainer();
			laneSet.getLanes().remove(lane);
			
			if(laneSet.getLanes().size() == 0) {
				EObject container = laneSet.eContainer();
				if(container instanceof Process) {
					Process p = (Process) container;
					p.getLaneSets().remove(laneSet);
				}
			}
			
			List<Lane> lanes = getTargetLanes(targetLane);
			lanes.add(lane);
			
			if(lanes.size() == 1) {
				for(FlowNode flowNode : targetLane.getFlowNodeRefs()) {
					flowNode.getLanes().add(lane);
				}
			}
		}
		
		private List<Lane> getTargetLanes(Lane targetLane) {
			if(targetLane.getChildLaneSet() == null) {
				targetLane.setChildLaneSet(ModelHandler.FACTORY.createLaneSet());
			}
			
			return targetLane.getChildLaneSet().getLanes();
		}
	}
	
	private class MoveFromLane implements MoveStrategy {

		@Override
		public boolean canMoveShape(IMoveShapeContext context) {
			if (context.getTargetContainer().equals(getDiagram()))
				return true;

			return false;
		}

		@Override
		public void postMoveShape(IMoveShapeContext context) {
		}
	}
}