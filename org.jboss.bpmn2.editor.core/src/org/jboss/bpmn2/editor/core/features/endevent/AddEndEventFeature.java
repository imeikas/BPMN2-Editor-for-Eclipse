package org.jboss.bpmn2.editor.core.features.endevent;

import org.eclipse.bpmn2.EndEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.jboss.bpmn2.editor.core.features.AbstractAddEventFeature;

public class AddEndEventFeature extends AbstractAddEventFeature {

	public AddEndEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean isEndEvent = context.getNewObject() instanceof EndEvent;
		boolean intoDiagram = context.getTargetContainer() instanceof Diagram;
		return isEndEvent && intoDiagram;
	}

	@Override
	protected void enhanceEllipse(Ellipse e) {
		e.setLineWidth(3);
	}
}
