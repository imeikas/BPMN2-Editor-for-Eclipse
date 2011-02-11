package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.EndEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateEndEventFeature extends AbstractCreateFlowElementFeature<EndEvent> {
	
	public CreateEndEventFeature(IFeatureProvider fp) {
		super(fp, "End Event", "Indicates the end of a process or choreography");
	}
	
	@Override
    protected EndEvent createFlowElement(ICreateContext context) {
		EndEvent end = ModelHandler.FACTORY.createEndEvent();
		end.setName("End");
		return end;
    }
	
	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_END_EVENT;
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); // FIXME
	}
}
