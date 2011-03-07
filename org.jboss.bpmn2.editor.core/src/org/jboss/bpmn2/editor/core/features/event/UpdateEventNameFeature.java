package org.jboss.bpmn2.editor.core.features.event;

import static org.jboss.bpmn2.editor.core.features.event.AddEventFeature.EVENT_ELEMENT;
import static org.jboss.bpmn2.editor.core.features.event.AddEventFeature.EVENT_TEXT;
import static org.jboss.bpmn2.editor.core.utils.FeatureSupport.getShape;

import org.eclipse.bpmn2.Event;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class UpdateEventNameFeature extends AbstractUpdateFeature {

	public UpdateEventNameFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return BusinessObjectUtil.containsElementOfType(context.getPictogramElement(), Event.class);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		ContainerShape container = (ContainerShape) context.getPictogramElement();
		Event event = (Event) BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), Event.class);

		Shape textShape = getShape(container, EVENT_ELEMENT, EVENT_TEXT);
		String name = ((Text) textShape.getGraphicsAlgorithm()).getValue();

		if (event.getName() != null) {
			return event.getName().equals(name) ? Reason.createFalseReason() : Reason.createTrueReason();
		} else if (name != null) {
			return name.equals(event.getName()) ? Reason.createFalseReason() : Reason.createTrueReason();
		} else {
			return Reason.createFalseReason();
		}
	}

	@Override
	public boolean update(IUpdateContext context) {
		ContainerShape container = (ContainerShape) context.getPictogramElement();
		Event event = (Event) BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), Event.class);
		Shape textShape = getShape(container, EVENT_ELEMENT, EVENT_TEXT);
		((Text) textShape.getGraphicsAlgorithm()).setValue(event.getName());
		layoutPictogramElement(context.getPictogramElement());
		return true;
	}
}