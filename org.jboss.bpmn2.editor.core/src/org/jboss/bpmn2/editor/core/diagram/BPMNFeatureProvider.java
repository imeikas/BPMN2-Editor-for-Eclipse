package org.jboss.bpmn2.editor.core.diagram;

import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;
import org.jboss.bpmn2.editor.core.features.AddExclusiveGatewayFeature;
import org.jboss.bpmn2.editor.core.features.AddSequenceFlowFeature;
import org.jboss.bpmn2.editor.core.features.AddTaskFeature;
import org.jboss.bpmn2.editor.core.features.CreateExclusiveGatewayFeature;
import org.jboss.bpmn2.editor.core.features.CreateTaskFeature;
import org.jboss.bpmn2.editor.core.features.CreateSequenceFlowFeature;
import org.jboss.bpmn2.editor.core.features.DirectEditTaskFeature;
import org.jboss.bpmn2.editor.core.features.UpdateTaskFeature;

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
		if (newObject instanceof Task)
			return new AddTaskFeature(this);
		if (newObject instanceof SequenceFlow)
			return new AddSequenceFlowFeature(this);
		if (newObject instanceof ExclusiveGateway)
			return new AddExclusiveGatewayFeature(this);
		return super.getAddFeature(context);
	}

	@Override
	public ICreateFeature[] getCreateFeatures() {
		return new ICreateFeature[] { new CreateTaskFeature(this), new CreateExclusiveGatewayFeature(this) };
	}

	@Override
	public IUpdateFeature getUpdateFeature(IUpdateContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();
		if (pictogramElement instanceof ContainerShape) {
			Object bo = getBusinessObjectForPictogramElement(pictogramElement);
			if (bo instanceof Task) {
				return new UpdateTaskFeature(this);
			}
		}
		return super.getUpdateFeature(context);
	}

	@Override
	public ICreateConnectionFeature[] getCreateConnectionFeatures() {
		return new ICreateConnectionFeature[] { new CreateSequenceFlowFeature(this) };
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pe);
		if (bo instanceof Task) {
			return new DirectEditTaskFeature(this);
		}
		return super.getDirectEditingFeature(context);
	}
}
