package org.jboss.bpmn2.editor.core.features.startevent;

import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.jboss.bpmn2.editor.core.features.AbstractAddEventFeature;

public class AddStartEventFeature extends AbstractAddEventFeature {
	
	public AddStartEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean isStartEvent = context.getNewObject() instanceof StartEvent;
		boolean intoDiagram = context.getTargetContainer() instanceof Diagram;
		return isStartEvent && intoDiagram;
	}
}
