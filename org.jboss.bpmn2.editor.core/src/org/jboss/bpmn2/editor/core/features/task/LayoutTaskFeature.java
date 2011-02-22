package org.jboss.bpmn2.editor.core.features.task;

import static org.jboss.bpmn2.editor.core.features.task.SizeConstants.*;

import java.util.Iterator;

import org.eclipse.bpmn2.Task;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

public class LayoutTaskFeature extends AbstractLayoutFeature {

	public LayoutTaskFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		PictogramElement pictoElem = context.getPictogramElement();
		if (!(pictoElem instanceof ContainerShape)) {
			return false;
		}
		EList<EObject> businessObjs = pictoElem.getLink().getBusinessObjects();
		return businessObjs.size() == 1 && businessObjs.get(0) instanceof Task;
	}

	@Override
	public boolean layout(ILayoutContext context) {
		boolean changed = false;

		ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
		GraphicsAlgorithm containerGa = containerShape.getGraphicsAlgorithm();
		IGaService gaService = Graphiti.getGaService();

		if (containerGa.getWidth() < WIDTH_MIN) {
			containerGa.setWidth(WIDTH_MIN);
			changed = true;
		}

		if (containerGa.getHeight() < HEIGHT_MIN) {
			containerGa.setHeight(HEIGHT_MIN);
			changed = true;
		}

		int containerWidth = containerGa.getWidth();
		Iterator<Shape> iterator = containerShape.getChildren().iterator();
		while (iterator.hasNext()) {
			Shape shape = (Shape) iterator.next();
			Object bo = getBusinessObjectForPictogramElement(shape);
			if (bo == null || !(bo instanceof Task))
				continue;
			GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
			IDimension size = gaService.calculateSize(ga);
			if (containerWidth != size.getWidth()) {
				gaService.setWidth(ga, containerWidth);
				changed = true;
			}
		}

		return changed;
	}
}