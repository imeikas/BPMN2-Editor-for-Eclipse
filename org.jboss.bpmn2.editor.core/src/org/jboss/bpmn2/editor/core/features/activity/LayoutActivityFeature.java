package org.jboss.bpmn2.editor.core.features.activity;

import java.util.Iterator;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.di.DIUtils;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.utils.ShapeUtil;

public class LayoutActivityFeature extends AbstractLayoutFeature {

	public LayoutActivityFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		Object bo = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), BaseElement.class);
		return bo != null && bo instanceof Activity;
	}

	@Override
	public boolean layout(ILayoutContext context) {
		boolean changed = false;

		ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
		GraphicsAlgorithm parentGa = containerShape.getGraphicsAlgorithm();

		Iterator<Shape> iterator = Graphiti.getPeService().getAllContainedShapes(containerShape).iterator();
		while (iterator.hasNext()) {
			Shape shape = iterator.next();
			GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
			IGaService gaService = Graphiti.getGaService();

			int newWidth = parentGa.getWidth();
			int newHeight = parentGa.getHeight() - ShapeUtil.ACTIVITY_BOTTOM_PADDING;

			String markerProperty = Graphiti.getPeService()
					.getPropertyValue(shape, ShapeUtil.ACTIVITY_MARKER_CONTAINER);
			if (markerProperty != null && new Boolean(markerProperty)) {
				int x = (newWidth / 2) - (ga.getWidth() / 2);
				int y = newHeight - ga.getHeight() - 3 - getMarkerContainerOffset();
				gaService.setLocation(ga, x, y);
				changed = true;
				continue;
			}

			Object[] objects = getAllBusinessObjectsForPictogramElement(shape);
			for (Object bo : objects) {
				if (bo instanceof BoundaryEvent) {
					layoutPictogramElement(shape);
					changed = true;
					continue;
				} else if (bo instanceof Activity && ga instanceof RoundedRectangle) {
					gaService.setSize(ga, newWidth, newHeight);
					layoutInRectangle((RoundedRectangle) ga);
					changed = true;
					continue;
				}
				if (layoutHook(shape, ga, bo, newWidth, newHeight)) {
					changed = true;
				}
			}
		}

		DIUtils.updateDIShape(getDiagram(), containerShape, Activity.class, ShapeUtil.ACTIVITY_BOTTOM_PADDING);
		return changed;
	}

	protected int getMarkerContainerOffset() {
		return 0;
	}

	protected void layoutInRectangle(RoundedRectangle rect) {
	}

	protected boolean layoutHook(Shape shape, GraphicsAlgorithm ga, Object bo, int newWidth, int newHeight) {
		return false;
	}
}