package org.jboss.bpmn2.editor.core.features.event.end;

import org.eclipse.bpmn2.EndEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.jboss.bpmn2.editor.core.features.event.AbstractLayoutEventFeature;

public class LayoutEndEventFeature extends AbstractLayoutEventFeature {

	public LayoutEndEventFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    protected boolean isInstanceOf(Object businessObject) {
	    return businessObject instanceof EndEvent;
    }
}
