package org.jboss.bpmn2.editor.core.features;

import static org.jboss.bpmn2.editor.core.utils.FeatureSupport.getShape;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.AbstractText;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class UpdateFlowElementNameFeature extends AbstractUpdateFeature {

	public static final String TEXT_ELEMENT = "baseelement.text";

	public UpdateFlowElementNameFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return BusinessObjectUtil.containsElementOfType(context.getPictogramElement(), FlowElement.class);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		ContainerShape container = (ContainerShape) context.getPictogramElement();

		FlowElement element = (FlowElement) BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(),
		        FlowElement.class);

		Shape textShape = getShape(container, TEXT_ELEMENT, Boolean.toString(true));
		String name = ((Text) textShape.getGraphicsAlgorithm()).getValue();

		if (element.getName() != null) {
			return element.getName().equals(name) ? Reason.createFalseReason() : Reason.createTrueReason();
		} else if (name != null) {
			return name.equals(element.getName()) ? Reason.createFalseReason() : Reason.createTrueReason();
		} else {
			return Reason.createFalseReason();
		}
	}

	@Override
	public boolean update(IUpdateContext context) {
		ContainerShape container = (ContainerShape) context.getPictogramElement();
		FlowElement element = (Event) BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), FlowElement.class);
		Shape textShape = getShape(container, TEXT_ELEMENT, Boolean.toString(true));
		((AbstractText) textShape.getGraphicsAlgorithm()).setValue(element.getName());
		layoutPictogramElement(context.getPictogramElement());
		return true;
	}
}