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
package org.eclipse.bpmn2.modeler.ui.features.event;

import static org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil.EVENT_SIZE;
import static org.eclipse.bpmn2.modeler.ui.features.event.BoundaryEventFeatureContainer.BOUNDARY_EVENT_CANCEL;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.modeler.core.di.DIImport;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmnAddFeature;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
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

public class BoundaryEventAddFeature extends AbstractBpmnAddFeature {

	public static final String BOUNDARY_EVENT_RELATIVE_Y = "boundary.event.relative.y";

	private final IPeService peService = Graphiti.getPeService();
	private final IGaService gaService = Graphiti.getGaService();

	public BoundaryEventAddFeature(IFeatureProvider fp) {
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
		ContainerShape target = importing ? context.getTargetContainer() : (ContainerShape) context
		        .getTargetContainer().eContainer();

		ContainerShape containerShape = peService.createContainerShape(target, true);
		Ellipse ellipse = gaService.createEllipse(containerShape);
		StyleUtil.applyBGStyle(ellipse, this);

		if (importing) { // if loading from DI then place according to context
			gaService.setLocationAndSize(ellipse, context.getX(), context.getY(), EVENT_SIZE, EVENT_SIZE);
		} else { // otherwise place it in the center of shape for user to adjust it
			GraphicsAlgorithm ga = context.getTargetContainer().getGraphicsAlgorithm();
			int x = ga.getX() + context.getX() - (EVENT_SIZE / 2);
			int y = ga.getY() + context.getY() - (EVENT_SIZE / 2);
			gaService.setLocationAndSize(ellipse, x, y, EVENT_SIZE, EVENT_SIZE);
		}

		Ellipse circle = GraphicsUtil.createIntermediateEventCircle(ellipse);
		circle.setStyle(StyleUtil.getStyleForClass(getDiagram()));
		createDIShape(containerShape, event);

		ChopboxAnchor anchor = peService.createChopboxAnchor(containerShape);
		anchor.setReferencedGraphicsAlgorithm(ellipse);
		AnchorUtil.addFixedPointAnchors(containerShape, ellipse);

		Activity activity = event.getAttachedToRef();
		PictogramElement foundElem = BusinessObjectUtil.getFirstBaseElementFromDiagram(getDiagram(), activity);
		if (foundElem != null && foundElem instanceof ContainerShape) {
			ContainerShape activityContainer = (ContainerShape) foundElem;
			PositionOnLine pos = BoundaryEventPositionHelper.getPositionOnLineUsingBPMNShape(containerShape,
			        activityContainer);
			BoundaryEventPositionHelper.assignPositionOnLineProperty(containerShape, pos);
		}

		peService.setPropertyValue(containerShape, BOUNDARY_EVENT_CANCEL, Boolean.toString(true));

		link(containerShape, event);
		updatePictogramElement(containerShape);
		return containerShape;
	}
}