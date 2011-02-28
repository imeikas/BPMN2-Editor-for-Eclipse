package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.Event;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public abstract class AbstractCreateEventFeature extends AbstractCreateFlowElementFeature<Event> {
	
	public AbstractCreateEventFeature(IFeatureProvider fp, String name, String description) {
	    super(fp, name, description);
    }
	
	abstract String getStencilImageId();
	
	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_START_EVENT;
	};
	
	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); //FIXME
	}
}