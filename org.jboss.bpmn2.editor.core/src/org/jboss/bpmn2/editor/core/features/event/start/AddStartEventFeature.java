package org.jboss.bpmn2.editor.core.features.event.start;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.jboss.bpmn2.editor.core.features.event.AbstractAddEventFeature;

public class AddStartEventFeature extends AbstractAddEventFeature {
	
	public AddStartEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
    protected Class<? extends BaseElement> getBPMNClass() {
	    return StartEvent.class;
    }
}
