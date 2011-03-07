package org.jboss.bpmn2.editor.core.features.data;

import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.jboss.bpmn2.editor.core.features.DefaultBpmnMoveFeature;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;

public abstract class AbstractDataFeatureContainer implements FeatureContainer {
	
	public static final String COLLECTION_PROPERTY = "isCollection";
	public static final String HIDEABLE_PROPERTY = "hideable";
	
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
	    return new DefaultBpmnMoveFeature(fp);
    }

	@Override
    public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
		return new DefaultResizeShapeFeature(fp) {
			@Override
			public boolean canResizeShape(IResizeShapeContext context) {
				return false;
			}
		};
    }
}
