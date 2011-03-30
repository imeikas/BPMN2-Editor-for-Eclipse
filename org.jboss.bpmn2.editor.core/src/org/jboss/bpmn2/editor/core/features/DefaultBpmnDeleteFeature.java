package org.jboss.bpmn2.editor.core.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.ui.features.DefaultDeleteFeature;

public class DefaultBpmnDeleteFeature extends DefaultDeleteFeature {

	public DefaultBpmnDeleteFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected boolean getUserDecision() {
		return true;
	}

}
