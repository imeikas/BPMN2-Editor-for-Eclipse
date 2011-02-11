package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateMessageStartEvent extends AbstractCreateFlowElementFeature<StartEvent> {

	public CreateMessageStartEvent(IFeatureProvider fp, String name, String description) {
	    super(fp, "Message Start Event", "A message arrives from other pool and starts a process");
    }

	@Override
    protected StartEvent createFlowElement(ICreateContext context) {
		StartEvent event = ModelHandler.FACTORY.createStartEvent();
		MessageEventDefinition eventDefinition = ModelHandler.FACTORY.createMessageEventDefinition();
		event.getEventDefinitions().add(eventDefinition);
	    return event;
    }
}
