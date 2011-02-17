package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.ShapeUtil.Envelope;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class AddMessageStartEventDefinition extends AbstractAddEventFeature {
	
	
	public AddMessageStartEventDefinition(IFeatureProvider fp) {
	    super(fp);
    }
	
	@Override
	protected void decorateEllipse(Ellipse e) {
		Envelope envelope = ShapeUtil.createEnvelope(e);
		envelope.rect.setFilled(false);
		envelope.rect.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		envelope.line.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
	}
	
	@Override
    protected Class<? extends Event> getEventClass() {
	    return StartEvent.class;
    }
	
	public static boolean isDefinitionsMatch(StartEvent e) {
		if (e.getEventDefinitions() == null || e.getEventDefinitions().isEmpty() || e.getEventDefinitions().size() > 1)
			return false;
		return e.getEventDefinitions().get(0) instanceof MessageEventDefinition;
    }
}
