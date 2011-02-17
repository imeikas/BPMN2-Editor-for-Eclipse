package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.ConditionalEventDefinition;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateConditionalStartEventFeature extends AbstractCreateFlowElementFeature<StartEvent> {

	public CreateConditionalStartEventFeature(IFeatureProvider fp) {
	    super(fp, "Conditional Start Event", "Triggered when specified condition becomes true");
    }

	@Override
    protected StartEvent createFlowElement(ICreateContext context) {
		StartEvent event = ModelHandler.FACTORY.createStartEvent();
		event.setName("Condtional Start Event");
		ConditionalEventDefinition definition = ModelHandler.FACTORY.createConditionalEventDefinition();
		event.getEventDefinitions().add(definition);
	    return event;
    }
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_CONDITION;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); //FIXME
	}
}
