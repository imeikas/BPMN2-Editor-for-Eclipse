package org.jboss.bpmn2.editor.core.features.participant;

import java.util.Iterator;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Participant;
import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.di.DIUtils;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class LayoutParticipantFeature extends AbstractLayoutFeature {

	public LayoutParticipantFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		Object bo = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), BaseElement.class);
		return bo != null && bo instanceof Participant;
	}

	@Override
	public boolean layout(ILayoutContext context) {
		boolean changed = false;

		ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
		DIUtils.updateDIShape(getDiagram(), containerShape, Participant.class);

		GraphicsAlgorithm containerGa = containerShape.getGraphicsAlgorithm();
		IGaService gaService = Graphiti.getGaService();

		int containerHeight = containerGa.getHeight();
		Iterator<Shape> iterator = containerShape.getChildren().iterator();
		while (iterator.hasNext()) {
			Shape shape = iterator.next();
			GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
			IDimension size = gaService.calculateSize(ga);
			if (containerHeight != size.getHeight()) {
				if (ga instanceof Polyline) {
					Polyline line = (Polyline) ga;
					Point firstPoint = line.getPoints().get(0);
					Point newPoint = gaService.createPoint(firstPoint.getX(), containerHeight);
					line.getPoints().set(1, newPoint);
					changed = true;
				} else {
					gaService.setHeight(ga, containerHeight);
					changed = true;
				}
			}
		}

		return changed;
	}

}
