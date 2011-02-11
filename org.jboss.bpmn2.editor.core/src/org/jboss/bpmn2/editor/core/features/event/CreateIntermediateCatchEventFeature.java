package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateIntermediateCatchEventFeature extends AbstractCreateFlowElementFeature<IntermediateCatchEvent> {

	public CreateIntermediateCatchEventFeature(IFeatureProvider fp) {
		super(fp, "Catch Event", "Token remains at the event until event trigger will occur");
	}

	@Override
	protected IntermediateCatchEvent createFlowElement(ICreateContext context) {
		IntermediateCatchEvent event = ModelHandler.FACTORY.createIntermediateCatchEvent();
		event.setName("Catch");
		return event;
	}
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_INTERMEDIATE_CATCH_EVENT;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); // FIXME
	}
}
