package org.jboss.bpmn2.editor.core.features.activity.task;

import static org.jboss.bpmn2.editor.core.features.activity.task.SizeConstants.HEIGHT_MIN;
import static org.jboss.bpmn2.editor.core.features.activity.task.SizeConstants.PADDING_BOTTOM;
import static org.jboss.bpmn2.editor.core.features.activity.task.SizeConstants.WIDTH_MIN;

import java.util.Iterator;

import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.Task;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
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
		ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
		GraphicsAlgorithm containerGa = containerShape.getGraphicsAlgorithm();
		IGaService gaService = Graphiti.getGaService();

		if (containerGa.getWidth() < WIDTH_MIN) {
			containerGa.setWidth(WIDTH_MIN);
		}

		if (containerGa.getHeight() < HEIGHT_MIN) {
			containerGa.setHeight(HEIGHT_MIN);
		}
		
		int newWidth = containerGa.getWidth();
		int newHeight = containerGa.getHeight() - PADDING_BOTTOM;
		
		Iterator<Shape> iterator = Graphiti.getPeService().getAllContainedShapes(containerShape).iterator();
		
		RoundedRectangle rect = (RoundedRectangle) iterator.next().getGraphicsAlgorithm();
		gaService.setSize(rect, newWidth, newHeight);
		
		Text text = (Text) iterator.next().getGraphicsAlgorithm();
		gaService.setSize(text, newWidth, text.getHeight());
		
		while (iterator.hasNext()) {
			Shape shape = (Shape) iterator.next();
			Object bo = getBusinessObjectForPictogramElement(shape);
			if (bo != null && bo instanceof BoundaryEvent) {
				layoutPictogramElement(shape);
			}
		}

		return true;
	}
}