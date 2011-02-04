package org.jboss.bpmn2.editor.core.features.event.end;

import org.eclipse.bpmn2.EndEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.jboss.bpmn2.editor.core.features.UpdateFeature;

public class UpdateEndEventFeature extends UpdateFeature {

	public UpdateEndEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof EndEvent;
	}
}
