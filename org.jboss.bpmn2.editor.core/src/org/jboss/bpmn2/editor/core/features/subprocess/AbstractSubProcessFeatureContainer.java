package org.jboss.bpmn2.editor.core.features.subprocess;

import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;

public abstract class AbstractSubProcessFeatureContainer implements FeatureContainer {

	@Override
    public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
	    return null;
    }

	@Override
    public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
	    return null;
    }

    public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
	    return new LayoutSubProcessFeature(fp);
    }

	@Override
    public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
	    return null;
    }

	@Override
    public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
		return null;
    }
}