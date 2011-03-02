package org.jboss.bpmn2.editor.core.features;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class MoveFlowNodeFeature extends DefaultMoveShapeFeature {

	private final List<Algorithm> algorithms;

	private AlgorithmContainer algorithmContainer;

	protected FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};

	public MoveFlowNodeFeature(IFeatureProvider fp) {
		super(fp);
		algorithms = new ArrayList<MoveFlowNodeFeature.Algorithm>();
		algorithms.add(new FromLaneAlgorithm());
		algorithms.add(new ToLaneAlgorithm());
		algorithms.add(new FromParticipantAlgorithm());
		algorithms.add(new ToParticipantAlgorithm());
		algorithms.add(new FromSubProcessAlgorithm());
		algorithms.add(new ToSubProcess());
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		if (!(getBusinessObjectForPictogramElement(context.getShape()) instanceof FlowNode)) {
			return false;
		}

		try {
			ModelHandler handler = support.getModelHanderInstance(getDiagram());

			algorithmContainer = getAlgorithmContainer(context);

			if (algorithmContainer.isEmpty()) {
				return super.canMoveShape(context);
			}

			return algorithmContainer.isMoveAllowed(getSourceBo(context, handler), getTargetBo(context, handler));
		} catch (IOException e) {
			Activator.logError(e);
		}

		return false;
	}

	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		if (algorithmContainer == null) {
			super.postMoveShape(context);
			return;
		}
		try {
			ModelHandler handler = support.getModelHanderInstance(getDiagram());
			Object[] node = getAllBusinessObjectsForPictogramElement(context.getShape());
			for (Object object : node) {
				if (object instanceof FlowNode) {
					algorithmContainer.move(((FlowNode) object), getSourceBo(context, handler),
							getTargetBo(context, handler));
				} else if (object instanceof BPMNShape) {
					Bounds bounds = ((BPMNShape) object).getBounds();
					bounds.setX(bounds.getX() - context.getDeltaX());
					bounds.setY(bounds.getY() - context.getDeltaY());
				}
			}
		} catch (Exception e) {
			Activator.logError(e);
		}
	}

	private Object getSourceBo(IMoveShapeContext context, ModelHandler handler) {
		return context.getSourceContainer().equals(getDiagram()) ? handler.getInternalParticipant()
				: getBusinessObjectForPictogramElement(context.getSourceContainer());
	}

	private Object getTargetBo(IMoveShapeContext context, ModelHandler handler) {
		return context.getTargetContainer().equals(getDiagram()) ? handler.getInternalParticipant()
				: getBusinessObjectForPictogramElement(context.getTargetContainer());
	}

	private boolean isSourceParticipant(IMoveShapeContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getSourceContainer());
		return context.getSourceContainer().equals(getDiagram()) || (bo != null && bo instanceof Participant);
	}

	private boolean isSourceLane(IMoveShapeContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getSourceContainer());
		return bo != null && bo instanceof Lane;
	}

	class AlgorithmContainer {
		public Algorithm fromAlgorithm;
		public Algorithm toAlgorithm;

		public AlgorithmContainer(Algorithm fromAlgorithm, Algorithm toAlgorithm) {
			this.fromAlgorithm = fromAlgorithm;
			this.toAlgorithm = toAlgorithm;
		}

		boolean isMoveAllowed(Object source, Object target) {
			return fromAlgorithm.isMoveAllowed(source, target) && toAlgorithm.isMoveAllowed(source, target);
		}

		void move(FlowNode node, Object source, Object target) {
			fromAlgorithm.move(node, source, target);
			toAlgorithm.move(node, source, target);
		}

		boolean isEmpty() {
			return fromAlgorithm == null || toAlgorithm == null;
		}
	}

	private AlgorithmContainer getAlgorithmContainer(IMoveShapeContext context) {
		Algorithm fromAlgorithm = null;
		Algorithm toAlgorithm = null;

		for (Algorithm a : algorithms) {
			if (a.canApplyTo(context)) {
				switch (a.getType()) {
				case Algorithm.TYPE_FROM:
					fromAlgorithm = a;
					break;
				case Algorithm.TYPE_TO:
					toAlgorithm = a;
					break;
				}
			}
		}

		return new AlgorithmContainer(fromAlgorithm, toAlgorithm);
	}

	interface Algorithm {

		int TYPE_FROM = 0;

		int TYPE_TO = 1;

		int getType();

		boolean canApplyTo(IMoveShapeContext context);

		boolean isMoveAllowed(Object source, Object target);

		void move(FlowNode node, Object source, Object target);
	}

	abstract class DefaultAlgorithm implements Algorithm {

		@Override
		public boolean isMoveAllowed(Object source, Object target) {
			return true;
		}

		@Override
		public void move(FlowNode node, Object source, Object target) {
			try {
				ModelHandler handler = support.getModelHanderInstance(getDiagram());
				handler.moveFlowNode(node, source, target);
			} catch (IOException e) {
				Activator.logError(e);
			}
		}
	}

	class FromLaneAlgorithm extends DefaultAlgorithm {

		@Override
		public int getType() {
			return TYPE_FROM;
		}

		@Override
		public boolean canApplyTo(IMoveShapeContext context) {
			return isSourceLane(context);
		}

		@Override
		public void move(FlowNode node, Object source, Object target) {
			Lane lane = (Lane) source;
			lane.getFlowNodeRefs().remove(node);
			node.getLanes().remove(lane);
		}
	}

	class ToLaneAlgorithm extends DefaultAlgorithm {

		@Override
		public int getType() {
			return TYPE_TO;
		}

		@Override
		public boolean canApplyTo(IMoveShapeContext context) {
			return support.isTargetLane(context);
		}

		@Override
		public boolean isMoveAllowed(Object source, Object target) {
			Lane lane = (Lane) target;
			return lane.getChildLaneSet() == null || lane.getChildLaneSet().getLanes().isEmpty();
		}

		@Override
		public void move(FlowNode node, Object source, Object target) {
			Lane lane = (Lane) target;
			lane.getFlowNodeRefs().add(node);
			node.getLanes().add(lane);
			super.move(node, source, target);
		}
	}

	class FromParticipantAlgorithm extends DefaultAlgorithm {

		@Override
		public int getType() {
			return TYPE_FROM;
		}

		@Override
		public boolean canApplyTo(IMoveShapeContext context) {
			return isSourceParticipant(context);
		}

		@Override
		public void move(FlowNode node, Object source, Object target) {
			// DO NOTHING HERE
		}
	}

	class ToParticipantAlgorithm extends DefaultAlgorithm {

		@Override
		public int getType() {
			return TYPE_TO;
		}

		@Override
		public boolean canApplyTo(IMoveShapeContext context) {
			return context.getTargetContainer().equals(getDiagram()) || support.isTargetParticipant(context);
		}

		@Override
		public boolean isMoveAllowed(Object source, Object target) {
			try {
				Participant p = (Participant) target;
				if (p.equals(support.getModelHanderInstance(getDiagram()).getInternalParticipant())) {
					return true;
				}
				if (p.getProcessRef() == null) {
					return true;
				}
				if (p.getProcessRef().getLaneSets().isEmpty()) {
					return true;
				}
			} catch (Exception e) {
				Activator.logError(e);
			}
			return false;
		}
	}

	class FromSubProcessAlgorithm extends DefaultAlgorithm {

		@Override
		public int getType() {
			return TYPE_FROM;
		}

		@Override
		public boolean canApplyTo(IMoveShapeContext context) {
			Object bo = getBusinessObjectForPictogramElement(context.getSourceContainer());
			return bo != null && bo instanceof SubProcess;
		}

		@Override
		public void move(FlowNode node, Object source, Object target) {
		}
	}

	class ToSubProcess extends DefaultAlgorithm {

		@Override
		public int getType() {
			return TYPE_TO;
		}

		@Override
		public boolean canApplyTo(IMoveShapeContext context) {
			Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
			return bo != null && bo instanceof SubProcess;
		}
	}
}