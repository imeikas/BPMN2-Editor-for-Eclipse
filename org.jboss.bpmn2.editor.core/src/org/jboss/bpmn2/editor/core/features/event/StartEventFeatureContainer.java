package org.jboss.bpmn2.editor.core.features.event;

import javax.xml.stream.events.StartElement;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class StartEventFeatureContainer extends AbstractEventFeatureContainer {

	@Override
    public boolean canApplyTo(BaseElement element) {
	    return element instanceof StartElement;
    }

	@Override
    public ICreateFeature getCreateFeature(IFeatureProvider fp) {
	    return new CreateStartEventFeature(fp);
    }

	@Override
    public IAddFeature getAddFeature(IFeatureProvider fp) {
	    return new AddEventFeature(fp);
    }
	
	public static class CreateStartEventFeature extends AbstractCreateEventFeature {
		
		public CreateStartEventFeature(IFeatureProvider fp) {
			super(fp, "Start Event", "Indicates the start of a process or choreography");
		}
		
		@Override
	    protected Event createFlowElement(ICreateContext context) {
			StartEvent start = ModelHandler.FACTORY.createStartEvent();
			start.setName("Start");
			return start;
	    }
		
		@Override
        String getStencilImageId() {
	        return ImageProvider.IMG_16_START_EVENT;
        }
	}
}