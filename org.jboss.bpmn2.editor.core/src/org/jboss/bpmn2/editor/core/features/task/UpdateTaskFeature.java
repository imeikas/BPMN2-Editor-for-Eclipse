package org.jboss.bpmn2.editor.core.features.task;

import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.jboss.bpmn2.editor.core.features.AbstractBaseElementUpdateFeature;

public class UpdateTaskFeature extends AbstractBaseElementUpdateFeature {

	public UpdateTaskFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof Task;
	}

}
