package org.jboss.bpmn2.editor.ui.features.activity.subprocess;

import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.jboss.bpmn2.editor.core.features.activity.LayoutActivityFeature;
import org.jboss.bpmn2.editor.ui.features.activity.AbstractActivityFeatureContainer;

public abstract class AbstractSubProcessFeatureContainer extends AbstractActivityFeatureContainer {

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return new LayoutActivityFeature(fp);
	}
}