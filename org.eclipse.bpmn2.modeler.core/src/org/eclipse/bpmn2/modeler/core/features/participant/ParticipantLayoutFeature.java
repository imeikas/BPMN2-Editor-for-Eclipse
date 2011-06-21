/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.features.participant;

import java.util.Iterator;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

public class ParticipantLayoutFeature extends AbstractLayoutFeature {

	public ParticipantLayoutFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		Object bo = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), BaseElement.class);
		return bo != null && bo instanceof Participant;
	}

	@Override
	public boolean layout(ILayoutContext context) {
		ContainerShape containerShape = (ContainerShape) context.getPictogramElement();

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
				} else if (ga instanceof Text) {
					gaService.setHeight(ga, containerHeight);
				}
			}
		}

		Shape shape = FeatureSupport.getShape(containerShape, ParticipantMultiplicityUpdateFeature.MULTIPLICITY_MARKER,
				Boolean.toString(true));
		if (shape != null) {
			GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
			int x = (containerGa.getWidth() / 2) - 10;
			int y = containerGa.getHeight() - 20;
			gaService.setLocation(ga, x, y);
		}

		DIUtils.updateDIShape(containerShape);
		return true;
	}
}