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
package org.eclipse.bpmn2.modeler.core.features.activity;

import java.util.Iterator;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.features.event.AbstractBoundaryEventOperation;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

public class ActivityLayoutFeature extends AbstractLayoutFeature {

	public ActivityLayoutFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		Object bo = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), BaseElement.class);
		return bo != null && bo instanceof Activity;
	}

	@Override
	public boolean layout(ILayoutContext context) {
		ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
		GraphicsAlgorithm parentGa = containerShape.getGraphicsAlgorithm();

		Iterator<Shape> iterator = Graphiti.getPeService().getAllContainedShapes(containerShape).iterator();
		while (iterator.hasNext()) {
			Shape shape = iterator.next();
			GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
			IGaService gaService = Graphiti.getGaService();

			int newWidth = parentGa.getWidth();
			int newHeight = parentGa.getHeight();

			String markerProperty = Graphiti.getPeService().getPropertyValue(shape,
					GraphicsUtil.ACTIVITY_MARKER_CONTAINER);
			if (markerProperty != null && new Boolean(markerProperty)) {
				int x = (newWidth / 2) - (ga.getWidth() / 2);
				int y = newHeight - ga.getHeight() - 3 - getMarkerContainerOffset();
				gaService.setLocation(ga, x, y);
				continue;
			}

			Shape rectShape = FeatureSupport.getShape(containerShape, "activity", Boolean.toString(true));
			gaService.setSize(rectShape.getGraphicsAlgorithm(), newWidth, newHeight);
			layoutInRectangle((RoundedRectangle) rectShape.getGraphicsAlgorithm());

			Object[] objects = getAllBusinessObjectsForPictogramElement(shape);
			for (Object bo : objects) {
				layoutHook(shape, ga, bo, newWidth, newHeight);
			}
		}

		Activity activity = BusinessObjectUtil.getFirstElementOfType(containerShape, Activity.class);
		new AbstractBoundaryEventOperation() {
			@Override
			protected void doWorkInternal(ContainerShape container) {
				layoutPictogramElement(container);
			}
		}.doWork(activity, getDiagram());

		DIUtils.updateDIShape(containerShape);
		return true;
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