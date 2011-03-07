package org.jboss.bpmn2.editor.ui.features.data;

import org.eclipse.graphiti.features.IDeleteFeature;
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
import org.jboss.bpmn2.editor.core.features.UpdateBaseElementNameFeature;
import org.jboss.bpmn2.editor.core.utils.GraphicsUtil;
import org.jboss.bpmn2.editor.ui.features.LayoutBaseElementTextFeature;

public abstract class AbstractDataFeatureContainer implements FeatureContainer {
	
	@Override
    public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
	    return new UpdateBaseElementNameFeature(fp);
    }

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
    public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
	    return new LayoutBaseElementTextFeature(fp) {
			
			@Override
			public int getMinimumWidth() {
				return GraphicsUtil.DATA_WIDTH;
			}
		};
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

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider context) {
		return null;
	}
}
