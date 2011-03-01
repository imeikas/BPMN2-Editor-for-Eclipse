package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.jboss.bpmn2.editor.core.features.AbstractBaseElementUpdateFeature;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.features.DirectEditFlowElementFeature;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;
import org.jboss.bpmn2.editor.core.features.MoveFlowNodeFeature;

public abstract class AbstractEventFeatureContainer implements FeatureContainer {

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		return new AbstractBaseElementUpdateFeature(fp) {

			@Override
			public boolean canUpdate(IUpdateContext context) {
				Object o = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), BaseElement.class);
				return o != null && o instanceof BaseElement && canApplyTo((BaseElement) o);
			}
		};
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
		return new DirectEditFlowElementFeature(fp);
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
		return new MoveFlowNodeFeature(fp);
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
