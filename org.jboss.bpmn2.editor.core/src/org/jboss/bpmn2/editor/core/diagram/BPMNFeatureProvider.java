package org.jboss.bpmn2.editor.core.diagram;

import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;
import org.jboss.bpmn2.editor.core.features.MoveFlowNodeFeature;
import org.jboss.bpmn2.editor.core.features.event.end.AddEndEventFeature;
import org.jboss.bpmn2.editor.core.features.event.end.CreateEndEventFeature;
import org.jboss.bpmn2.editor.core.features.event.end.DirectEditEndEventFeature;
import org.jboss.bpmn2.editor.core.features.event.end.LayoutEndEventFeature;
import org.jboss.bpmn2.editor.core.features.event.end.UpdateEndEventFeature;
import org.jboss.bpmn2.editor.core.features.event.start.AddStartEventFeature;
import org.jboss.bpmn2.editor.core.features.event.start.CreateStartEventFeature;
import org.jboss.bpmn2.editor.core.features.event.start.DirectEditStartEventFeature;
import org.jboss.bpmn2.editor.core.features.event.start.LayoutStartEventFeature;
import org.jboss.bpmn2.editor.core.features.event.start.UpdateStartEventFeature;
import org.jboss.bpmn2.editor.core.features.exclusivegateway.AddExclusiveGatewayFeature;
import org.jboss.bpmn2.editor.core.features.exclusivegateway.CreateExclusiveGatewayFeature;
import org.jboss.bpmn2.editor.core.features.exclusivegateway.DirectEditExclusiveGatewayFeature;
import org.jboss.bpmn2.editor.core.features.exclusivegateway.UpdateExclusiveGatewayFeature;
import org.jboss.bpmn2.editor.core.features.lane.AddLaneFeature;
import org.jboss.bpmn2.editor.core.features.lane.CreateLaneFeature;
import org.jboss.bpmn2.editor.core.features.lane.LayoutLaneFeature;
import org.jboss.bpmn2.editor.core.features.lane.MoveLaneFeature;
import org.jboss.bpmn2.editor.core.features.sequenceflow.AddSequenceFlowFeature;
import org.jboss.bpmn2.editor.core.features.sequenceflow.CreateSequenceFlowFeature;
import org.jboss.bpmn2.editor.core.features.task.AddTaskFeature;
import org.jboss.bpmn2.editor.core.features.task.CreateTaskFeature;
import org.jboss.bpmn2.editor.core.features.task.DirectEditTaskFeature;
import org.jboss.bpmn2.editor.core.features.task.LayoutTaskFeature;
import org.jboss.bpmn2.editor.core.features.task.UpdateTaskFeature;

/**
 * Determines what kinds of business objects can be added to a diagram.
 * 
 * @author imeikas
 * 
 */
public class BPMNFeatureProvider extends DefaultFeatureProvider {

	public BPMNFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);
	}

	@Override
	public IAddFeature getAddFeature(IAddContext context) {
		Object newObject = context.getNewObject();
		if (newObject instanceof Task) {
			return new AddTaskFeature(this);
		} else if (newObject instanceof SequenceFlow) {
			return new AddSequenceFlowFeature(this);
		} else if (newObject instanceof ExclusiveGateway) {
			return new AddExclusiveGatewayFeature(this);
		} else if (newObject instanceof StartEvent) {
			return new AddStartEventFeature(this);
		} else if (newObject instanceof EndEvent) {
			return new AddEndEventFeature(this);
		} else if (newObject instanceof Lane) {
			return new AddLaneFeature(this);
		}
		return super.getAddFeature(context);
	}

	@Override
	public ICreateFeature[] getCreateFeatures() {
		// if you change this part significantly, check that you won't break Bpmn2Preferences
		return new ICreateFeature[] { new CreateStartEventFeature(this), new CreateEndEventFeature(this),
		        new CreateTaskFeature(this), new CreateExclusiveGatewayFeature(this), new CreateLaneFeature(this) };
	}

	@Override
	public IUpdateFeature getUpdateFeature(IUpdateContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();
		if (pictogramElement instanceof ContainerShape) {
			Object bo = getBusinessObjectForPictogramElement(pictogramElement);
			if (bo instanceof Task) {
				return new UpdateTaskFeature(this);
			} else if (bo instanceof StartEvent) {
				return new UpdateStartEventFeature(this);
			} else if (bo instanceof EndEvent) {
				return new UpdateEndEventFeature(this);
			} else if (bo instanceof ExclusiveGateway) {
				return new UpdateExclusiveGatewayFeature(this);
			}
		}
		return super.getUpdateFeature(context);
	}

	@Override
	public ICreateConnectionFeature[] getCreateConnectionFeatures() {
		// if you change this part significantly, check that you won't break Bpmn2Preferences
		return new ICreateConnectionFeature[] { new CreateSequenceFlowFeature(this) };
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pe);
		if (bo instanceof Task) {
			return new DirectEditTaskFeature(this);
		} else if (bo instanceof StartEvent) {
			return new DirectEditStartEventFeature(this);
		} else if (bo instanceof EndEvent) {
			return new DirectEditEndEventFeature(this);
		} else if (bo instanceof ExclusiveGateway) {
			return new DirectEditExclusiveGatewayFeature(this);
		} else {
			return super.getDirectEditingFeature(context);
		}
	}

	@Override
	public ILayoutFeature getLayoutFeature(ILayoutContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pictogramElement);
		if (bo instanceof Task) {
			return new LayoutTaskFeature(this);
		} else if (bo instanceof StartEvent) {
			return new LayoutStartEventFeature(this);
		} else if (bo instanceof EndEvent) {
			return new LayoutEndEventFeature(this);
		} else if (bo instanceof Lane) {
			return new LayoutLaneFeature(this);
		} else {
			return super.getLayoutFeature(context);
		}
	}

	@Override
	public IMoveShapeFeature getMoveShapeFeature(IMoveShapeContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getShape());
		if (bo instanceof FlowNode) {
			return new MoveFlowNodeFeature(this);
		} else if (bo instanceof Lane) {
			return new MoveLaneFeature(this);
		} else {
			return super.getMoveShapeFeature(context);
		}
	}
}
