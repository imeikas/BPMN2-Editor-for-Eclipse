package org.jboss.bpmn2.editor.core.features.gateway.exclusive;

import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.jboss.bpmn2.editor.core.features.AbstractBaseElementUpdateFeature;

public class UpdateExclusiveGatewayFeature extends AbstractBaseElementUpdateFeature {

	public UpdateExclusiveGatewayFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof ExclusiveGateway;
	}
}
