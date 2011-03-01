package org.jboss.bpmn2.editor.core.features.activity.task;

import java.util.Iterator;

import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

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
		return BusinessObjectUtil.containsElementOfType(pictoElem, Task.class);
	}

	@Override
	public boolean layout(ILayoutContext context) {
		ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
		GraphicsAlgorithm containerGa = containerShape.getGraphicsAlgorithm();
		IGaService gaService = Graphiti.getGaService();

		if (containerGa.getWidth() < 50) {
			containerGa.setWidth(50);
		}

		if (containerGa.getHeight() < 25) {
			containerGa.setHeight(25);
		}

		int newWidth = containerGa.getWidth();
		int newHeight = containerGa.getHeight() - ShapeUtil.ACTIVITY_BOTTOM_PADDING;
		
		Iterator<Shape> iterator = Graphiti.getPeService().getAllContainedShapes(containerShape).iterator();

		RoundedRectangle rect = (RoundedRectangle) iterator.next().getGraphicsAlgorithm();
		gaService.setSize(rect, newWidth, newHeight);

		Text text = (Text) iterator.next().getGraphicsAlgorithm();
		gaService.setSize(text, newWidth, text.getHeight());

		while (iterator.hasNext()) {
			Shape shape = iterator.next();
			Object[] objects = getAllBusinessObjectsForPictogramElement(shape);
			for (Object bo : objects) {
				if (bo instanceof BPMNShape) {
					Bounds bounds = ((BPMNShape) bo).getBounds();
					bounds.setWidth(newWidth);
					bounds.setHeight(newHeight);
				} else if (bo instanceof BoundaryEvent) {
					layoutPictogramElement(shape);
				}
			}

		}

		return true;
	}
}