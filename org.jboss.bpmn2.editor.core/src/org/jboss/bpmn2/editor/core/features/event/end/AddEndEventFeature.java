package org.jboss.bpmn2.editor.core.features.event.end;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.jboss.bpmn2.editor.core.features.event.AbstractAddEventFeature;

public class AddEndEventFeature extends AbstractAddEventFeature {

	public AddEndEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void enhanceEllipse(Ellipse e) {
		e.setLineWidth(3);
	}

	@Override
    protected Class<? extends BaseElement> getEventClass() {
	    return EndEvent.class;
    }
}
