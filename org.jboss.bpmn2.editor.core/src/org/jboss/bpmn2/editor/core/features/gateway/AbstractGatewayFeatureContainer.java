package org.jboss.bpmn2.editor.core.features.gateway;

import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;
import org.jboss.bpmn2.editor.core.features.MoveFlowNodeFeature;

public abstract class AbstractGatewayFeatureContainer implements FeatureContainer {

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		return null; // TODO
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
		return null; // TODO
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return null; // TODO
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

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider context) {
		return null;
	}
}