package org.jboss.bpmn2.editor.core.features;

import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;

public abstract class ConnectionFeatureContainer implements FeatureContainer {
	
	public abstract ICreateConnectionFeature getCreateConnectionFeature(IFeatureProvider fp);
	
	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
	    return null;
	}

	@Override
    public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
	    return null;
    }

	@Override
    public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
	    return null;
    }

	@Override
    public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
	    return null;
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