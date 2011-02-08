package org.jboss.bpmn2.editor.core.features.lane;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Process;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class MoveFromDiagramFeature extends MoveLaneFeature {
	
	public MoveFromDiagramFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		if (context.getSourceContainer().equals(context.getTargetContainer()))
			return true;

		if (support.isTargetLane(context))
			return true;

		return false;
	}

	@Override
	protected void internalMove(IMoveShapeContext context) {
		ContainerShape targetContainer = context.getTargetContainer();

		Lane lane = (Lane) getBusinessObjectForPictogramElement(context.getShape());
		Lane targetLane = (Lane) getBusinessObjectForPictogramElement(targetContainer);
		List<Shape> shapes = getFlowNodeShapes(context, targetLane);

		modifyModelStructure(targetLane, lane);

		IGaService gaService = Graphiti.getGaService();
		GraphicsAlgorithm ga = context.getShape().getGraphicsAlgorithm();

		GraphicsAlgorithm targetGa = targetContainer.getGraphicsAlgorithm();
		int width = targetGa.getWidth();
		int height = targetGa.getHeight();
		int numberOfLanes = targetLane.getChildLaneSet().getLanes().size();

		if (numberOfLanes == 1) {
			gaService.setLocationAndSize(ga, 15, 0, width - 15, height);
			for (Shape s : shapes) {
				Graphiti.getPeService().sendToFront(s);
				s.setContainer((ContainerShape) context.getShape());
			}
		} else {
			gaService.setLocationAndSize(ga, 15, height - 1, width - 15, ga.getHeight() + 1);
			ILocation location = Graphiti.getLayoutService().getLocationRelativeToDiagram(context.getShape());
			support.resizeLanesRecursively(targetContainer, ga.getHeight() - 1, location.getX(), location.getY());
		}
	}

	private void modifyModelStructure(Lane targetLane, Lane lane) {
		LaneSet laneSet = (LaneSet) lane.eContainer();
		laneSet.getLanes().remove(lane);

		if (laneSet.getLanes().size() == 0) {
			EObject container = laneSet.eContainer();
			if (container instanceof Process) {
				Process p = (Process) container;
				p.getLaneSets().remove(laneSet);
			}
		}

		if (targetLane.getChildLaneSet() == null) {
			targetLane.setChildLaneSet(ModelHandler.FACTORY.createLaneSet());
		}
		
		List<Lane> lanes = targetLane.getChildLaneSet().getLanes();
		lanes.add(lane);

		if (lanes.size() == 1) {
			List<FlowNode> flowNodeRefs = targetLane.getFlowNodeRefs();
			for (FlowNode flowNode : flowNodeRefs) {
				flowNode.getLanes().add(lane);
			}
			targetLane.getFlowNodeRefs().clear();
		}
	}

	private List<Shape> getFlowNodeShapes(IMoveShapeContext context, Lane lane) {
		List<FlowNode> nodes = lane.getFlowNodeRefs();
		List<Shape> shapes = new ArrayList<Shape>();
		for (Shape s : context.getTargetContainer().getChildren()) {
			Object bo = getBusinessObjectForPictogramElement(s);
			if (bo != null && nodes.contains(bo)) {
				shapes.add(s);
			}
		}
		return shapes;
	}
}