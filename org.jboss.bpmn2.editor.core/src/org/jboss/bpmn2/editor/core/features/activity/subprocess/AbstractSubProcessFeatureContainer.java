package org.jboss.bpmn2.editor.core.features.activity.subprocess;

import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
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

	@Override
    public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
	    return null;
    }

	@Override
    public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
		return null;
    }
}