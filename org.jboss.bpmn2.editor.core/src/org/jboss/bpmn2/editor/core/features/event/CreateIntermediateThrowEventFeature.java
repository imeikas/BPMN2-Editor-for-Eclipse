package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateIntermediateThrowEventFeature extends AbstractCreateFlowElementFeature<IntermediateThrowEvent> {

	public CreateIntermediateThrowEventFeature(IFeatureProvider fp) {
	    super(fp, "Throw Event", "Throws the event trigger and the event immediately occurs");
    }

	@Override
    protected IntermediateThrowEvent createFlowElement(ICreateContext context) {
		IntermediateThrowEvent event = ModelHandler.FACTORY.createIntermediateThrowEvent();
		event.setName("Throw");
		return event;
    }
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_INTERMEDIATE_THORW_EVENT;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); // FIXME
	}
}
