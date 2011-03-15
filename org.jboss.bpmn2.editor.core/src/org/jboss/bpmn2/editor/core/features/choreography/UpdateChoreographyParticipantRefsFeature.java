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

import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.BODY_LINE_LEFT;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.BODY_LINE_RIGHT;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.BOTTOM_BAND;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.BOTTOM_BAND_TEXT;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.CHOREOGRAPHY_ACTIVITY_PROPERTY;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.INITIATING_PARTICIPANT_REF;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.PARTICIPANT_BAND_HEIGHT;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.PARTICIPANT_REF;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.PARTICIPANT_REF_ID;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.PARTICIPANT_REF_NUM;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.TOP_BAND;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.TOP_BAND_TEXT;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.bpmn2.Participant;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.IColorConstant;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.utils.FeatureSupport;
import org.jboss.bpmn2.editor.core.utils.StyleUtil;
import org.jboss.bpmn2.editor.core.utils.Tuple;

public class UpdateChoreographyParticipantRefsFeature extends AbstractUpdateFeature {

	private static final IPeService peService = Graphiti.getPeService();
	private static final IGaService gaService = Graphiti.getGaService();

	public UpdateChoreographyParticipantRefsFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return BusinessObjectUtil.containsElementOfType(context.getPictogramElement(), ChoreographyActivity.class);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		ChoreographyActivity activity = (ChoreographyActivity) BusinessObjectUtil.getFirstElementOfType(
		        context.getPictogramElement(), ChoreographyActivity.class);

		if (activity.getParticipantRefs().size() != getParticipantRefNumber(context)) {
			return Reason.createTrueReason();
		} else {
			return Reason.createFalseReason();
		}
	}

	@Override
	public boolean update(IUpdateContext context) {
		ChoreographyActivity activity = (ChoreographyActivity) BusinessObjectUtil.getFirstElementOfType(
		        context.getPictogramElement(), ChoreographyActivity.class);

		clearChoreographyActivity(context);
		buildChoreographyActivity(activity, context);

		ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
		Shape shape = FeatureSupport.getShape(containerShape, CHOREOGRAPHY_ACTIVITY_PROPERTY, BODY_LINE_LEFT);
		if (shape != null) {
			peService.sendToFront(shape);
		}
		shape = FeatureSupport.getShape(containerShape, CHOREOGRAPHY_ACTIVITY_PROPERTY, BODY_LINE_RIGHT);
		if (shape != null) {
			peService.sendToFront(shape);
		}

		peService.setPropertyValue(context.getPictogramElement(), PARTICIPANT_REF_NUM,
		        Integer.toString(activity.getParticipantRefs().size()));
		return true;
	}

	private int getParticipantRefNumber(IUpdateContext context) {
		return Integer.parseInt(peService.getPropertyValue(context.getPictogramElement(), PARTICIPANT_REF_NUM));
	}

	private void clearChoreographyActivity(IUpdateContext context) {
		List<Shape> toBeRemoved = new ArrayList<Shape>();

		Iterator<Shape> iterator = peService.getAllContainedShapes((ContainerShape) context.getPictogramElement())
		        .iterator();
		while (iterator.hasNext()) {
			Shape shape = (Shape) iterator.next();
			String property = peService.getPropertyValue(shape, PARTICIPANT_REF);
			if (property != null && new Boolean(property)) {
				toBeRemoved.add(shape);
			}
		}

		int size = toBeRemoved.size();
		for (int i = 0; i < size; i++) {
			peService.deletePictogramElement(toBeRemoved.get(i));
		}

		Tuple<Shape, Shape> topAndBottomBands = getTopAndBottomBands(context);
		peService.setPropertyValue(topAndBottomBands.getFirst(), PARTICIPANT_REF_ID, Boolean.toString(false));
		peService.setPropertyValue(topAndBottomBands.getSecond(), PARTICIPANT_REF_ID, Boolean.toString(false));
	}

	private void buildChoreographyActivity(ChoreographyActivity task, IUpdateContext context) {
		List<Participant> participants = task.getParticipantRefs();
		int size = participants.size();
		Tuple<Text, Text> topAndBottom = getTopAndBottomTexts(context);
		Tuple<Shape, Shape> topAndBottomBands = getTopAndBottomBands(context);

		ContainerShape container = (ContainerShape) context.getPictogramElement();
		String participantRefProperty = peService.getPropertyValue(container, INITIATING_PARTICIPANT_REF);

		int h = 20;
		int w = container.getGraphicsAlgorithm().getWidth();
		int topY = PARTICIPANT_BAND_HEIGHT - 5;
		int bottomY = container.getGraphicsAlgorithm().getHeight() - topY - h;
		int location = 0;

		for (int i = 0; i < size; i++) {
			Participant participant = participants.get(i);

			if (i == 0) {
				if (showNames()) {
					topAndBottom.getFirst().setValue(participant.getName());
				}
				peService.setPropertyValue(topAndBottomBands.getFirst(), PARTICIPANT_REF_ID, participant.getId());
				if (participant.getId().equals(participantRefProperty)) {
					topAndBottomBands.getFirst().getGraphicsAlgorithm()
					        .setBackground(manageColor(IColorConstant.WHITE));
				} else {
					topAndBottomBands.getFirst().getGraphicsAlgorithm()
					        .setBackground(manageColor(IColorConstant.LIGHT_GRAY));
				}
				continue;
			}
			if (i == 1) {
				if (showNames()) {
					topAndBottom.getSecond().setValue(participant.getName());
				}
				peService.setPropertyValue(topAndBottomBands.getSecond(), PARTICIPANT_REF_ID, participant.getId());
				if (participant.getId().equals(participantRefProperty)) {
					topAndBottomBands.getSecond().getGraphicsAlgorithm()
					        .setBackground(manageColor(IColorConstant.WHITE));
				} else {
					topAndBottomBands.getSecond().getGraphicsAlgorithm()
					        .setBackground(manageColor(IColorConstant.LIGHT_GRAY));
				}
				continue;
			}

			Shape shape = peService.createShape(container, false);
			peService.setPropertyValue(shape, PARTICIPANT_REF, Boolean.toString(true));
			peService.setPropertyValue(shape, PARTICIPANT_REF_ID, participant.getId());
			Rectangle rect = gaService.createRectangle(shape);

			if (location == 0) {
				gaService.setLocationAndSize(rect, 0, topY, w, h);
				topY += h - 1;
				location += 1;
			} else {
				gaService.setLocationAndSize(rect, 0, bottomY, w, h);
				bottomY -= h - 1;
				location -= 1;
			}

			rect.setFilled(true);
			rect.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));

			if (participant.getId().equals(participantRefProperty)) {
				rect.setBackground(manageColor(IColorConstant.WHITE));
			} else {
				rect.setBackground(manageColor(IColorConstant.LIGHT_GRAY));
			}

			Text text = gaService.createText(rect);
			gaService.setLocationAndSize(text, 0, 0, w, h);
			text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
			text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
			text.setStyle(StyleUtil.getStyleForText(getDiagram()));
			if (showNames()) {
				text.setValue(participant.getName());
			}
		}

		if (size == 0 && showNames()) {
			topAndBottom.getFirst().setValue("Participant A");
			topAndBottom.getSecond().setValue("Participant B");
		}

		if (size == 1 && showNames()) {
			topAndBottom.getSecond().setValue("Participant B");
		}
	}

	private Tuple<Text, Text> getTopAndBottomTexts(IUpdateContext context) {
		Text top = null;
		Text bottom = null;
		Iterator<Shape> iterator = peService.getAllContainedShapes((ContainerShape) context.getPictogramElement())
		        .iterator();
		while (iterator.hasNext()) {
			Shape shape = (Shape) iterator.next();
			String property = peService.getPropertyValue(shape, CHOREOGRAPHY_ACTIVITY_PROPERTY);
			if (property == null) {
				continue;
			}
			if (property.equals(TOP_BAND_TEXT)) {
				top = (Text) shape.getGraphicsAlgorithm();
			} else if (property.equals(BOTTOM_BAND_TEXT)) {
				bottom = (Text) shape.getGraphicsAlgorithm();
			}
			if (top != null && bottom != null) {
				break;
			}
		}
		return new Tuple<Text, Text>(top, bottom);
	}

	private Tuple<Shape, Shape> getTopAndBottomBands(IUpdateContext context) {
		Shape top = null;
		Shape bottom = null;
		Iterator<Shape> iterator = peService.getAllContainedShapes((ContainerShape) context.getPictogramElement())
		        .iterator();
		while (iterator.hasNext()) {
			Shape shape = (Shape) iterator.next();
			String property = peService.getPropertyValue(shape, CHOREOGRAPHY_ACTIVITY_PROPERTY);
			if (property == null) {
				continue;
			}
			if (property.equals(TOP_BAND)) {
				top = shape;
			} else if (property.equals(BOTTOM_BAND)) {
				bottom = shape;
			}
			if (top != null && bottom != null) {
				break;
			}
		}
		return new Tuple<Shape, Shape>(top, bottom);
	}

	protected boolean showNames() {
		return true;
	}
}