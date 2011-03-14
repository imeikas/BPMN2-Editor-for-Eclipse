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

import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.BODY_BAND;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.BODY_BAND_TEXT;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.BOTTOM_BAND;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.BOTTOM_BAND_TEXT;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.CHOREOGRAPHY_ACTIVITY_PROPERTY;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.PARTICIPANT_BAND_HEIGHT;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.PARTICIPANT_REF;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.TOP_BAND;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.TOP_BAND_TEXT;

import java.util.Iterator;

import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class LayoutChoreographyFeature extends AbstractLayoutFeature {

	protected static IPeService peService = Graphiti.getPeService();
	protected static IGaService gaService = Graphiti.getGaService();

	public LayoutChoreographyFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		return BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), ChoreographyActivity.class) != null;
	}

	@Override
	public boolean layout(ILayoutContext context) {
		ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
		GraphicsAlgorithm parentGa = containerShape.getGraphicsAlgorithm();

		int newWidth = parentGa.getWidth();
		int newHeight = parentGa.getHeight();

		int topY = layoutParticipantComponents(containerShape, newWidth, newHeight);
		layoutMainComponents(containerShape, newWidth, newHeight, topY);

		return true;
	}

	private void layoutMainComponents(ContainerShape containerShape, int newWidth, int newHeight, int topY) {
		Iterator<Shape> iterator = peService.getAllContainedShapes(containerShape).iterator();
		while (iterator.hasNext()) {
			Shape shape = (Shape) iterator.next();
			String property = peService.getPropertyValue(shape, CHOREOGRAPHY_ACTIVITY_PROPERTY);
			if (property == null) {
				continue;
			}
			int h = PARTICIPANT_BAND_HEIGHT;
			GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
			if (property.equals(TOP_BAND)) {
				gaService.setLocationAndSize(ga, 0, 0, newWidth, h);
			} else if (property.equals(TOP_BAND_TEXT)) {
				gaService.setLocationAndSize(ga, 0, 0, newWidth, h);
			} else if (property.equals(BOTTOM_BAND)) {
				gaService.setLocationAndSize(ga, 0, newHeight - h, newWidth, h);
			} else if (property.equals(BOTTOM_BAND_TEXT)) {
				gaService.setLocationAndSize(ga, 0, newHeight - h, newWidth, h);
			} else if (property.equals(BODY_BAND)) {
				gaService.setLocationAndSize(ga, 0, h - 5, newWidth, newHeight - (2 * h) + 10);
			} else if (property.equals(BODY_BAND_TEXT)) {
				layoutBodyText(ga, newWidth, newHeight, h, topY);
			}
		}
	}

	private int layoutParticipantComponents(ContainerShape containerShape, int newWidth, int newHeight) {
		Iterator<Shape> iterator = peService.getAllContainedShapes(containerShape).iterator();

		int h = 20;
		int topY = PARTICIPANT_BAND_HEIGHT - 5;
		int bottomY = newHeight - topY - h;
		int location = 0;

		while (iterator.hasNext()) {
			Shape shape = (Shape) iterator.next();
			String property = peService.getPropertyValue(shape, PARTICIPANT_REF);
			if (property != null && new Boolean(property)) {
				GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
				if (location == 0) {
					gaService.setLocationAndSize(ga, 0, topY, newWidth, h);
					topY += h - 1;
					location += 1;
				} else {
					gaService.setLocationAndSize(ga, 0, bottomY, newWidth, h);
					bottomY -= h - 1;
					location -= 1;
				}
				ga.getGraphicsAlgorithmChildren().get(0).setWidth(newWidth);
			}
		}
		return topY + 5;
	}

	protected void layoutBodyText(GraphicsAlgorithm ga, int w, int h, int bandHeight, int y) {
		gaService.setLocationAndSize(ga, 0, bandHeight - 5, w, h - (2 * bandHeight) + 10);
	}
}