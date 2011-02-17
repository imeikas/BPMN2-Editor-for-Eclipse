package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateMessageStartEventFeature extends AbstractCreateFlowElementFeature<StartEvent> {

	public CreateMessageStartEventFeature(IFeatureProvider fp) {
	    super(fp, "Message Start Event", "A message arrives from other pool and starts a process");
    }

	@Override
    protected StartEvent createFlowElement(ICreateContext context) {
		StartEvent event = ModelHandler.FACTORY.createStartEvent();
		event.setName("Message Start Event");
		MessageEventDefinition definition = ModelHandler.FACTORY.createMessageEventDefinition();
		event.getEventDefinitions().add(definition);
	    return event;
    }
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_MESSAGE;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); //FIXME
	}
}
