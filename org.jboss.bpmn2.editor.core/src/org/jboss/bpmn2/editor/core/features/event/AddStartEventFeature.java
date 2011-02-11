package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IFeatureProvider;

public class AddStartEventFeature extends AbstractAddEventFeature {
	
	public AddStartEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
    protected Class<? extends Event> getEventClass() {
	    return StartEvent.class;
    }
}
