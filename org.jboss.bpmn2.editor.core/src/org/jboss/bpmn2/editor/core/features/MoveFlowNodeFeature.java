package org.jboss.bpmn2.editor.core.features;

import java.io.IOException;

import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Participant;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class MoveFlowNodeFeature extends DefaultMoveShapeFeature {
	
	private MoveStrategy strategy;

	protected FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};

	public MoveFlowNodeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		if (!(getBusinessObjectForPictogramElement(context.getShape()) instanceof FlowNode))
			return false;

		strategy = getMoveStrategy(context);

		if (strategy == null) {
			return super.canMoveShape(context);
		}

		return strategy.canMoveShape(context);
	}

	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		if (strategy == null) {
			super.postMoveShape(context);
			return;
		}
		strategy.postMoveShape(context);
	}

	private abstract class MoveStrategy {

		FlowNode node;
		Participant source;
		Participant target;

		public MoveStrategy(IMoveShapeContext context, Participant source, Participant target) {
			this.source = source;
			this.target = target;
			this.node = (FlowNode) getBusinessObjectForPictogramElement(context.getShape());
		}

		void postMoveShape(IMoveShapeContext context) {
			try {
				ModelHandler handler = support.getModelHanderInstance(getDiagram());
				handler.moveFlowNode(node, source, target);
			} catch (IOException e) {
				Activator.logError(e);
			}
		}

		abstract boolean canMoveShape(IMoveShapeContext context);
	}

	private class FromParticipantToParticipant extends MoveStrategy {

		public FromParticipantToParticipant(IMoveShapeContext context, Participant source, Participant target) {
			super(context, source, target);
		}

		@Override
		public boolean canMoveShape(IMoveShapeContext context) {
			return canMoveToParticipant(context, target);
		}
	}

	private class FromLaneToLane extends MoveStrategy {

		public FromLaneToLane(IMoveShapeContext context, Participant source, Participant target) {
			super(context, source, target);
		}

		@Override
		public boolean canMoveShape(IMoveShapeContext context) {
			return canMoveToLane(context);
		}

		@Override
		public void postMoveShape(IMoveShapeContext context) {
			super.postMoveShape(context);
			Lane sourceLane = (Lane) getBusinessObjectForPictogramElement(context.getSourceContainer());
			Lane targetLane = (Lane) getBusinessObjectForPictogramElement(context.getTargetContainer());
			removeFromLane(node, sourceLane);
			addToLane(node, targetLane);
		}
	}

	private class FromLaneToParticipant extends MoveStrategy {

		public FromLaneToParticipant(IMoveShapeContext context, Participant source, Participant target) {
			super(context, source, target);
		}

		@Override
		public boolean canMoveShape(IMoveShapeContext context) {
			return canMoveToParticipant(context, target);
		}
		
		@Override
		public void postMoveShape(IMoveShapeContext context) {
			super.postMoveShape(context);
			Lane sourceLane = (Lane) getBusinessObjectForPictogramElement(context.getSourceContainer());
			removeFromLane(node, sourceLane);
		}
	}

	private class FromParticipantToLane extends MoveStrategy {

		public FromParticipantToLane(IMoveShapeContext context, Participant source, Participant target) {
			super(context, source, target);
		}

		@Override
		public boolean canMoveShape(IMoveShapeContext context) {
			return canMoveToLane(context);
		}

		@Override
		public void postMoveShape(IMoveShapeContext context) {
			super.postMoveShape(context);
			Lane targetLane = (Lane) getBusinessObjectForPictogramElement(context.getTargetContainer());
			addToLane(node, targetLane);
		}
	}
	
	private void removeFromLane(FlowNode node, Lane lane) {
		lane.getFlowNodeRefs().remove(node);
		node.getLanes().remove(lane);
	}
	
	private void addToLane(FlowNode node, Lane lane) {
		lane.getFlowNodeRefs().add(node);
		node.getLanes().add(lane);
	}
	
	private boolean canMoveToParticipant(IMoveShapeContext context, Participant participant) {
		if (context.getTargetContainer().equals(getDiagram()))
			return true;
		if (participant.getProcessRef() == null)
			return true;
		if (participant.getProcessRef().getLaneSets().isEmpty())
			return true;
		return false;
	}

	private boolean canMoveToLane(IMoveShapeContext context) {
		Lane targetLane = (Lane) getBusinessObjectForPictogramElement(context.getTargetContainer());
		return targetLane.getChildLaneSet() == null || targetLane.getChildLaneSet().getLanes().isEmpty();
	}

	private MoveStrategy getMoveStrategy(IMoveShapeContext context) {
		try {
			ModelHandler handler = support.getModelHanderInstance(getDiagram());

			Object sourceBo = getBusinessObjectForPictogramElement(context.getSourceContainer());
			Object targetBo = getBusinessObjectForPictogramElement(context.getTargetContainer());

			if (context.getSourceContainer().equals(getDiagram())) {
				Participant source = handler.getParticipant(getDiagram());
				if (context.getTargetContainer().equals(getDiagram()))
					return null;
				if (support.isTargetLane(context))
					return new FromParticipantToLane(context, source, handler.getParticipant(targetBo));
				if (support.isTargetParticipant(context)) {
					return new FromParticipantToParticipant(context, source, handler.getParticipant(targetBo));
				}
			}
			if (isSourceLane(context)) {
				Participant source = handler.getParticipant(sourceBo);
				if (context.getTargetContainer().equals(getDiagram()))
					return new FromLaneToParticipant(context, source, handler.getParticipant(getDiagram()));
				if (support.isTargetLane(context))
					return new FromLaneToLane(context, source, handler.getParticipant(targetBo));
				if (support.isTargetParticipant(context))
					return new FromLaneToParticipant(context, source, handler.getParticipant(targetBo));
			}
			if (isSourceParticipant(context)) {
				Participant source = (Participant) sourceBo;
				if (context.getTargetContainer().equals(getDiagram()))
					return new FromParticipantToParticipant(context, source, handler.getParticipant(getDiagram()));
				if (support.isTargetLane(context))
					return new FromParticipantToLane(context, source, handler.getParticipant(targetBo));
				if (support.isTargetParticipant(context))
					return new FromParticipantToParticipant(context, source, (Participant) targetBo);
			}
		} catch (IOException e) {
			Activator.logError(e);
		}
		return null;
	}

	private boolean isSourceParticipant(IMoveShapeContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getSourceContainer());
		return bo != null && bo instanceof Participant;
	}

	private boolean isSourceLane(IMoveShapeContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getSourceContainer());
		return bo != null && bo instanceof Lane;
	}
}
