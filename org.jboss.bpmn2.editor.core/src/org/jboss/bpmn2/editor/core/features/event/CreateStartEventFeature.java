package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateStartEventFeature extends AbstractCreateFlowElementFeature<StartEvent> {
	
	public CreateStartEventFeature(IFeatureProvider fp) {
		super(fp, "Start Event", "Indicates the start of a process or choreography");
	}
	
	@Override
    protected StartEvent createFlowElement(ICreateContext context) {
		StartEvent start = ModelHandler.FACTORY.createStartEvent();
		start.setName("Start");
		if(support.isTargetLane(context)) {
			start.getLanes().add((Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
		}
		return start;
    }
	
	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_START_EVENT;
	};
	
	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); //FIXME
	}
}
