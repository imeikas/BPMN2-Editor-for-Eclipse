/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.bpmn2.editor.ui.features.event;

import static org.jboss.bpmn2.editor.core.utils.GraphicsUtil.EVENT_SIZE;
import static org.jboss.bpmn2.editor.ui.features.event.BoundaryEventFeatureContainer.BOUNDARY_EVENT_CANCEL;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.di.DIImport;
import org.jboss.bpmn2.editor.core.features.AbstractBpmnAddFeature;
import org.jboss.bpmn2.editor.core.utils.GraphicsUtil;
import org.jboss.bpmn2.editor.core.utils.StyleUtil;

public class AddBoundaryEventFeature extends AbstractBpmnAddFeature {

	public static final String BOUNDARY_EVENT_RELATIVE_X = "boundary.event.relative.x";
	public static final String BOUNDARY_EVENT_RELATIVE_Y = "boundary.event.relative.y";

	private final IPeService peService = Graphiti.getPeService();
	private final IGaService gaService = Graphiti.getGaService();

	public AddBoundaryEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		if (!(context.getNewObject() instanceof BoundaryEvent)) {
			return false;
		}

		Object prop = context.getProperty(DIImport.IMPORT_PROPERTY);
		if (prop != null && (Boolean) prop) {
			return true;
		}

		Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
		return bo != null && bo instanceof Activity;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		BoundaryEvent event = (BoundaryEvent) context.getNewObject();

		Object prop = context.getProperty(DIImport.IMPORT_PROPERTY);
		boolean importing = prop != null && (Boolean) prop;
		ContainerShape target = importing ? context.getTargetContainer() : getDiagram();

		ContainerShape containerShape = peService.createContainerShape(target, true);
		Ellipse ellipse = gaService.createEllipse(containerShape);
		StyleUtil.applyBGStyle(ellipse, this);

		if (importing) { // if loading from DI then place according to context
			gaService.setLocationAndSize(ellipse, context.getX(), context.getY(), EVENT_SIZE, EVENT_SIZE);
		} else { // otherwise place it in the center of shape for user to adjust it
			GraphicsAlgorithm ga = context.getTargetContainer().getGraphicsAlgorithm();
			int x = ga.getX() + (ga.getWidth() / 2) - (EVENT_SIZE / 2);
			int y = ga.getY() + (ga.getHeight() / 2) - (EVENT_SIZE / 2);
			gaService.setLocationAndSize(ellipse, x, y, EVENT_SIZE, EVENT_SIZE);
		}

		Ellipse circle = GraphicsUtil.createIntermediateEventCircle(ellipse);
		circle.setStyle(StyleUtil.getStyleForClass(getDiagram()));
		createDIShape(containerShape, event);

		ChopboxAnchor anchor = peService.createChopboxAnchor(containerShape);
		anchor.setReferencedGraphicsAlgorithm(ellipse);

		peService.setPropertyValue(containerShape, BOUNDARY_EVENT_CANCEL, Boolean.toString(true));
		peService.setPropertyValue(containerShape, BOUNDARY_EVENT_RELATIVE_X, calculateRelativeX(context));
		peService.setPropertyValue(containerShape, BOUNDARY_EVENT_RELATIVE_Y, calculateRelativeY(context));

		// layoutPictogramElement(context.getTargetContainer());
		return containerShape;
	}

	private String calculateRelativeX(IAddContext context) {
		ContainerShape container = context.getTargetContainer();
		ILocation loc = peService.getLocationRelativeToDiagram(container);

		int xContainer = loc.getX();
		int xEvent = context.getX();

		if (xContainer < xEvent) {
			return Integer.toString(xEvent);
		} else {
			return Integer.toString(xEvent - xContainer);
		}
	}

	private String calculateRelativeY(IAddContext context) {
		ContainerShape container = context.getTargetContainer();
		ILocation loc = peService.getLocationRelativeToDiagram(container);

		int yContainer = loc.getY();
		int yEvent = context.getY();

		if (yContainer < yEvent) {
			return Integer.toString(yEvent);
		} else {
			return Integer.toString(yEvent - yContainer);
		}
	}
}