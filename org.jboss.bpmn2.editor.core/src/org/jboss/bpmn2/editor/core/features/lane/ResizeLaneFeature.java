package org.jboss.bpmn2.editor.core.features.lane;

import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.di.DIUtils;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public class ResizeLaneFeature extends DefaultResizeShapeFeature {

	public ResizeLaneFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		boolean isLane = FeatureSupport.isLane(context.getPictogramElement());
		if (!isLane) {
			return false;
		}

		boolean isParentLane = FeatureSupport.isLane(((ContainerShape) context.getPictogramElement()).getContainer());
		if (!isParentLane) {
			return true;
		}

		if (context.getHeight() == -1 && context.getWidth() == -1) {
			return true;
		}

		GraphicsAlgorithm ga = ((ContainerShape) context.getPictogramElement()).getGraphicsAlgorithm();

		int i = compare(ga.getHeight(), ga.getWidth(), context.getHeight(), context.getWidth());

		Lane lane = (Lane) BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), Lane.class);

		if (i < 0 && lane.getFlowNodeRefs().size() == 0) {
			return true;
		}

		if (i > 0) {
			return true;
		}

		return true;
	}

	private int compare(int heightBefore, int widthBefore, int heightAfter, int widthAfter) {
		if (heightAfter > heightBefore || widthAfter > widthBefore) {
			return 1;
		}
		if (heightAfter < heightBefore || widthAfter < widthBefore) {
			return -1;
		}
		return 0;
	}

	@Override
	public void resizeShape(IResizeShapeContext context) {
		super.resizeShape(context);
		DIUtils.updateDIShape(getDiagram(), context.getPictogramElement(), Lane.class);
	}
}
