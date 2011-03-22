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
package org.jboss.bpmn2.editor.core.features.choreography;

import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.CHOREOGRAPHY_ACTIVITY_PROPERTY;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.INITIATING_PARTICIPANT_REF;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.PARTICIPANT_BAND_HEIGHT;

import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.IColorConstant;
import org.jboss.bpmn2.editor.core.features.AbstractBpmnAddFeature;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil;
import org.jboss.bpmn2.editor.core.utils.StyleUtil;

public abstract class AbstractChoreographyAddFeature extends AbstractBpmnAddFeature {

	protected static final IGaService gaService = Graphiti.getGaService();
	protected static final IPeService peService = Graphiti.getPeService();

	public AbstractChoreographyAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context.getTargetContainer().equals(getDiagram());
	}

	@Override
	public PictogramElement add(IAddContext context) {
		ChoreographyActivity choreography = (ChoreographyActivity) context.getNewObject();

		int width = context.getWidth() > 0 ? context.getWidth() : 100;
		int height = context.getHeight() > 0 ? context.getHeight() : 100;

		ContainerShape containerShape = peService.createContainerShape(context.getTargetContainer(), true);
		Rectangle invisibleRect = gaService.createInvisibleRectangle(containerShape);
		gaService.setLocationAndSize(invisibleRect, context.getX(), context.getY(), width, height);

		int bandHeight = PARTICIPANT_BAND_HEIGHT;

		Shape topRectShape = peService.createShape(containerShape, false);
		RoundedRectangle topRect = gaService.createRoundedRectangle(topRectShape, 5, 5);
		gaService.setLocationAndSize(topRect, 0, 0, width, bandHeight);
		topRect.setFilled(true);
		topRect.setBackground(manageColor(IColorConstant.LIGHT_GRAY));
		topRect.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		// peService.setPropertyValue(topRectShape, CHOREOGRAPHY_ACTIVITY_PROPERTY, TOP_BAND);

		Shape bottomRectShape = peService.createShape(containerShape, false);
		RoundedRectangle bottomRect = gaService.createRoundedRectangle(bottomRectShape, 5, 5);
		gaService.setLocationAndSize(bottomRect, 0, height - bandHeight, width, bandHeight);
		bottomRect.setFilled(true);
		bottomRect.setBackground(manageColor(IColorConstant.LIGHT_GRAY));
		bottomRect.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		// peService.setPropertyValue(bottomRectShape, CHOREOGRAPHY_ACTIVITY_PROPERTY, BOTTOM_BAND);

		Shape bodyShape = peService.createShape(containerShape, false);
		Rectangle body = gaService.createRectangle(bodyShape);
		gaService.setLocationAndSize(body, 0, bandHeight - 5, width, height - (2 * bandHeight) + 10);
		StyleUtil.applyBGStyle(body, this);
		// peService.setPropertyValue(bodyShape, CHOREOGRAPHY_ACTIVITY_PROPERTY, BODY_BAND);

		createTexts(containerShape, choreography, width, height, bandHeight);
		decorate(containerShape, width, height, bandHeight);

		peService.setPropertyValue(containerShape, ChoreographyProperties.PARTICIPANT_REF_IDS, Integer.toString(0));
		peService.setPropertyValue(containerShape, INITIATING_PARTICIPANT_REF, Boolean.toString(false));

		link(containerShape, choreography);
		peService.createChopboxAnchor(containerShape);
		AnchorUtil.addFixedPointAnchors(containerShape, invisibleRect);
		updatePictogramElement(containerShape);
		layoutPictogramElement(containerShape);
		createDIShape(containerShape, choreography);
		return containerShape;
	}

	protected void createTexts(ContainerShape containerShape, ChoreographyActivity choreography, int w, int h,
			int bandHeight) {
	}

	protected Text createText(ContainerShape container, String text, int x, int y, int w, int h, String propertyValue) {
		Shape shape = peService.createShape(container, false);
		Text bodyText = gaService.createText(shape);
		gaService.setLocationAndSize(bodyText, x, y, w, h);
		bodyText.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		bodyText.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
		peService.setPropertyValue(shape, CHOREOGRAPHY_ACTIVITY_PROPERTY, propertyValue);
		bodyText.setStyle(StyleUtil.getStyleForText(getDiagram()));
		bodyText.setValue(text);
		return bodyText;
	}

	protected void decorate(ContainerShape containerShape, int w, int h, int bandHeight) {
	}
}