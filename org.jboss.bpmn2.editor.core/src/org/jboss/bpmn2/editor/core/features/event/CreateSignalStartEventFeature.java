package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.SignalEventDefinition;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateSignalStartEventFeature extends AbstractCreateFlowElementFeature<StartEvent> {

	public CreateSignalStartEventFeature(IFeatureProvider fp) {
	    super(fp, "Signal Start Event", "Event triggered by a signal from another process");
    }

	@Override
    protected StartEvent createFlowElement(ICreateContext context) {
		StartEvent event = ModelHandler.FACTORY.createStartEvent();
	    event.setName("Signal Start Event");
	    SignalEventDefinition definition = ModelHandler.FACTORY.createSignalEventDefinition();
	    event.getEventDefinitions().add(definition);
	    return event;
    }
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_SIGNAL;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); //FIXME
	}
}
