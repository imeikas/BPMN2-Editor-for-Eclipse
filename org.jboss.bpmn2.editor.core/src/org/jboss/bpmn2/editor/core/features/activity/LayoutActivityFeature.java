package org.jboss.bpmn2.editor.core.features.activity;

import java.util.Iterator;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;

public abstract class LayoutActivityFeature extends AbstractLayoutFeature {

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
			Shape shape = (Shape) iterator.next();
			GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
			IGaService gaService = Graphiti.getGaService();
			
			String markerProperty = Graphiti.getPeService()
			        .getPropertyValue(shape, ShapeUtil.ACTIVITY_MARKER_CONTAINER);
			if (markerProperty != null && new Boolean(markerProperty)) {
				gaService.setLocation(ga, (parentGa.getWidth() / 2) - (ga.getWidth() / 2), 0);
				changed = true;
				continue;
			}

			EObject bo = BusinessObjectUtil.getFirstElementOfType(shape, BaseElement.class);

			if (bo != null && bo instanceof BoundaryEvent) {
				layoutPictogramElement(shape);
				changed = true;
				continue;
			}

			if (bo != null && bo instanceof Activity && ga instanceof RoundedRectangle) {
				changed = true;
				continue;
			}

			if (layoutHook(shape, ga, parentGa, bo)) {
				changed = true;
			}
		}

		return changed;
	}

	protected abstract boolean layoutHook(Shape shape, GraphicsAlgorithm ga, GraphicsAlgorithm parentGa, EObject bo);
}