package org.jboss.bpmn2.editor.core.features.event.start;

import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.jboss.bpmn2.editor.core.features.UpdateFeature;

public class UpdateStartEventFeature extends UpdateFeature {

	public UpdateStartEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof StartEvent;
	}
}
