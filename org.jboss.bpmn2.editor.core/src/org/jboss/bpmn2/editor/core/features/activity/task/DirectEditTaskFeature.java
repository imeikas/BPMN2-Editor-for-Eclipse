package org.jboss.bpmn2.editor.core.features.activity.task;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.jboss.bpmn2.editor.core.features.DirectEditFlowElementFeature;

public class DirectEditTaskFeature extends DirectEditFlowElementFeature {

	public DirectEditTaskFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String checkValueValid(String value, IDirectEditingContext context) {
		if (value.length() < 1) {
			return "Please enter any text as Task name.";
		} else if (value.contains("\n")) {
			return "Line breakes are not allowed in Task names.";
		}
		return null;
	}
}