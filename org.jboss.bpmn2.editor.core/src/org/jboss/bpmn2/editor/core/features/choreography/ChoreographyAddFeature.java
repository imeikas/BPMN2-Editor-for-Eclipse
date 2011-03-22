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

import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.ENVELOPE_HEIGHT_MODIFIER;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.ENV_H;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.ENV_W;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.INITIATING_PARTICIPANT_REF;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.PARTICIPANT_REF_IDS;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.R;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyUtil.drawMessageLink;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyUtil.drawMultiplicityMarkers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.ParticipantBandKind;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.di.DIImport;
import org.jboss.bpmn2.editor.core.features.AbstractBpmnAddFeature;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil.AnchorLocation;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil.BoundaryAnchor;
import org.jboss.bpmn2.editor.core.utils.StyleUtil;

public class ChoreographyAddFeature extends AbstractBpmnAddFeature {

	protected final IGaService gaService = Graphiti.getGaService();
	protected final IPeService peService = Graphiti.getPeService();

	public ChoreographyAddFeature(IFeatureProvider fp) {
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
		RoundedRectangle containerRect = gaService.createRoundedRectangle(containerShape, R, R);
		gaService.setLocationAndSize(containerRect, context.getX(), context.getY(), width, height);
		StyleUtil.applyBGStyle(containerRect, this);
		decorateContainerRect(containerRect);

		Object importProperty = context.getProperty(DIImport.IMPORT_PROPERTY);
		if (importProperty != null && (Boolean) importProperty) {
			addedFromImport(choreography, containerShape, context);
		} else {
			addedByUser(context);
		}

		peService.createChopboxAnchor(containerShape);
		AnchorUtil.addFixedPointAnchors(containerShape, containerRect);
		createDIShape(containerShape, choreography);
		return containerShape;
	}

	protected void decorateContainerRect(RoundedRectangle containerRect) {
	}

	protected void addedFromImport(ChoreographyActivity choreography, ContainerShape container, IAddContext context) {
		ModelHandler mh = null;

		try {
			mh = ModelHandler.getInstance(getDiagram());
		} catch (IOException e) {
			Activator.logError(e);
			return;
		}

		List<Participant> participants = choreography.getParticipantRefs();
		List<BPMNShape> shapes = mh.getAll(BPMNShape.class);
		List<BPMNShape> filteredShapes = new ArrayList<BPMNShape>();
		BPMNShape choreoBpmnShape = null;

		for (BPMNShape shape : shapes) {
			if (shape.getBpmnElement().equals(choreography)) {
				choreoBpmnShape = shape;
				break;
			}
		}

		for (BPMNShape shape : shapes) {
			if (participants.contains(shape.getBpmnElement())
					&& choreoBpmnShape.equals(shape.getChoreographyActivityShape())) {
				filteredShapes.add(shape);
			}
		}

		for (BPMNShape bpmnShape : filteredShapes) {
			ParticipantBandKind bandKind = bpmnShape.getParticipantBandKind();
			ContainerShape createdShape = ChoreographyUtil.createParticipantBandContainerShape(bandKind, container,
					bpmnShape);
			createDIShape(createdShape, bpmnShape.getBpmnElement(), bpmnShape);

			if (bpmnShape.isIsMessageVisible()) {

				boolean top = bandKind == ParticipantBandKind.TOP_INITIATING
						|| bandKind == ParticipantBandKind.TOP_NON_INITIATING;

				BoundaryAnchor anchor = AnchorUtil.getBoundaryAnchors(createdShape).get(
						top ? AnchorLocation.TOP : AnchorLocation.BOTTOM);

				Bounds bounds = bpmnShape.getBounds();

				int x = (int) (bounds.getX() + bounds.getWidth() / 2) - ENV_W / 2;
				int y = (int) (top ? bounds.getY() - ENVELOPE_HEIGHT_MODIFIER - ENV_H : bounds.getY()
						+ bounds.getHeight() + ENVELOPE_HEIGHT_MODIFIER);

				boolean filled = bandKind == ParticipantBandKind.TOP_NON_INITIATING
						|| bandKind == ParticipantBandKind.BOTTOM_NON_INITIATING;

				drawMessageLink(anchor, x, y, filled);
			}

			Participant p = (Participant) bpmnShape.getBpmnElement();
			if (p.getParticipantMultiplicity() != null && p.getParticipantMultiplicity().getMaximum() > 1) {
				drawMultiplicityMarkers(createdShape);
			}
		}

		peService.setPropertyValue(container, PARTICIPANT_REF_IDS, ChoreographyUtil.getParticipantRefIds(choreography));
		Participant initiatingParticipant = choreography.getInitiatingParticipantRef();
		String id = initiatingParticipant == null ? "null" : initiatingParticipant.getId();
		peService.setPropertyValue(container, INITIATING_PARTICIPANT_REF, id);
	}

	protected void addedByUser(IAddContext context) {
	}
}