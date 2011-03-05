package org.jboss.bpmn2.editor.core.features.activity.subprocess;

import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.jboss.bpmn2.editor.core.features.activity.AbstractActivityFeatureContainer;
import org.jboss.bpmn2.editor.core.features.activity.LayoutActivityFeature;

public abstract class AbstractSubProcessFeatureContainer extends AbstractActivityFeatureContainer {

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
		return null;
	}

	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return new LayoutActivityFeature(fp);
	}
}