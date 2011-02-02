package org.jboss.bpmn2.editor.core.features.task;

import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.jboss.bpmn2.editor.core.features.AbstractBPMNUpdateFeature;

public class UpdateTaskFeature extends AbstractBPMNUpdateFeature {

	public UpdateTaskFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof Task;
	}

}
