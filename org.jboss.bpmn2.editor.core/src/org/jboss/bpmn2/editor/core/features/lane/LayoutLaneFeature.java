package org.jboss.bpmn2.editor.core.features.lane;

import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.jboss.bpmn2.editor.core.di.DIUtils;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public class LayoutLaneFeature extends AbstractLayoutFeature {

	public LayoutLaneFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		return FeatureSupport.isLane(context.getPictogramElement());
	}

	@Override
	public boolean layout(ILayoutContext context) {
		FeatureSupport.redraw((ContainerShape) context.getPictogramElement());
		DIUtils.updateDIShape(getDiagram(), context.getPictogramElement(), Lane.class);
		return true;
	}
}
