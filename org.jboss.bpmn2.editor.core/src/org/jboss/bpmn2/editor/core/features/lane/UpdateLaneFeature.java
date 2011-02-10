package org.jboss.bpmn2.editor.core.features.lane;

import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.jboss.bpmn2.editor.core.features.AbstractBaseElementUpdateFeature;

public class UpdateLaneFeature extends AbstractBaseElementUpdateFeature {

	public UpdateLaneFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    public boolean canUpdate(IUpdateContext context) {
		return getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof Lane;
    }
}
