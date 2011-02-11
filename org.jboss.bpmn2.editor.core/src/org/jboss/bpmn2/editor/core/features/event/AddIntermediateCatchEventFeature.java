package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class AddIntermediateCatchEventFeature  extends AbstractAddEventFeature {

	public AddIntermediateCatchEventFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    protected Class<? extends Event> getEventClass() {
	    return IntermediateCatchEvent.class;
    }
	
	@Override
	protected void decorateEllipse(Ellipse e) {
		Ellipse circle = ShapeUtil.createTriggerCircle(e);
		circle.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
	}
}
