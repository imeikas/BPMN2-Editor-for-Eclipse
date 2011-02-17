package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateTimerStartEventFeature extends AbstractCreateFlowElementFeature<StartEvent> {

	public CreateTimerStartEventFeature(IFeatureProvider fp) {
	    super(fp, "Timer Start Event", "Event triggered by specific time or cycle");
    }

	@Override
    protected StartEvent createFlowElement(ICreateContext context) {
	    StartEvent event = ModelHandler.FACTORY.createStartEvent();
	    event.setName("Timer Start Event");
	    TimerEventDefinition definition = ModelHandler.FACTORY.createTimerEventDefinition();
	    event.getEventDefinitions().add(definition);
	    return event;
    }
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_TIMER;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); //FIXME
	}
}
