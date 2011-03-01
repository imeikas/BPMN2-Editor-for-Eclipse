package org.jboss.bpmn2.editor.core.features.lane;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public class ResizeLaneFeature extends DefaultResizeShapeFeature {

	private final FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return BusinessObjectUtil.getFirstElementOfType(element, BaseElement.class);
		}
	};

	public ResizeLaneFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		boolean isLane = support.isLane(context.getPictogramElement());
		if (!isLane) {
			return false;
		}

		boolean isParentLane = support.isLane(((ContainerShape) context.getPictogramElement()).getContainer());
		if (!isParentLane) {
			return true;
		}

		if (context.getHeight() == -1 && context.getWidth() == -1) {
			return true;
		}

		GraphicsAlgorithm ga = ((ContainerShape) context.getPictogramElement()).getGraphicsAlgorithm();

		int i = compare(ga.getHeight(), ga.getWidth(), context.getHeight(), context.getWidth());

		Lane lane = (Lane) support.getBusinessObject(context.getPictogramElement());

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
}
