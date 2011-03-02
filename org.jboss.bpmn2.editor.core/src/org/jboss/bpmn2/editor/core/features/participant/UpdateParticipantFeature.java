package org.jboss.bpmn2.editor.core.features.participant;

import org.eclipse.bpmn2.Participant;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.jboss.bpmn2.editor.core.features.AbstractBaseElementUpdateFeature;

public class UpdateParticipantFeature extends AbstractBaseElementUpdateFeature {

	public UpdateParticipantFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    public boolean canUpdate(IUpdateContext context) {
		return getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof Participant;
    }
}
