package org.jboss.bpmn2.editor.core.features.event.start;

import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.jboss.bpmn2.editor.core.features.event.AbstractLayoutEventFeature;

public class LayoutStartEventFeature extends AbstractLayoutEventFeature {

	public LayoutStartEventFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    protected boolean isInstanceOf(Object businessObject) {
	    return businessObject instanceof StartEvent;
    }
}
