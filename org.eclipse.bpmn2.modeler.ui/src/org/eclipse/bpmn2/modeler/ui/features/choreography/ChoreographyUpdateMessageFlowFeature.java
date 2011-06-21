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
package org.eclipse.bpmn2.modeler.ui.features.choreography;

import static org.eclipse.bpmn2.modeler.core.features.choreography.ChoreographyProperties.MESSAGE_REF_IDS;
import static org.eclipse.bpmn2.modeler.core.features.choreography.ChoreographyProperties.MESSAGE_VISIBLE;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.ChoreographyTask;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;

public class ChoreographyUpdateMessageFlowFeature extends AbstractUpdateFeature {

	private final IPeService peService = Graphiti.getPeService();

	public ChoreographyUpdateMessageFlowFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return BusinessObjectUtil.containsElementOfType(context.getPictogramElement(), ChoreographyTask.class);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		if (!BusinessObjectUtil.containsElementOfType(context.getPictogramElement(), ChoreographyTask.class)) {
			return Reason.createFalseReason();
		}

		ChoreographyTask choreography = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(),
				ChoreographyTask.class);

		String ids = peService.getPropertyValue(context.getPictogramElement(), MESSAGE_REF_IDS);
		String choreoIds = ChoreographyUtil.getMessageRefIds(choreography);

		if (ids.equals(choreoIds)) {
			return Reason.createFalseReason();
		}

		return Reason.createTrueReason();
	}

	@Override
	public boolean update(IUpdateContext context) {
		ContainerShape choreographyContainer = (ContainerShape) context.getPictogramElement();

		ChoreographyTask choreography = BusinessObjectUtil.getFirstElementOfType(choreographyContainer,
				ChoreographyTask.class);

		List<InteractionNode> sources = new ArrayList<InteractionNode>();
		for (MessageFlow message : choreography.getMessageFlowRef()) {
			sources.add(message.getSourceRef());
		}

		for (ContainerShape band : ChoreographyUtil.getParticipantBandContainerShapes(choreographyContainer)) {
			Participant participant = BusinessObjectUtil.getFirstElementOfType(band, Participant.class);
			BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(band, BPMNShape.class);
			if (!sources.contains(participant) && bpmnShape.isIsMessageVisible()) {
				bpmnShape.setIsMessageVisible(false);
				peService.setPropertyValue(context.getPictogramElement(), MESSAGE_VISIBLE, Boolean.toString(false));
			} else if (sources.contains(participant) && !bpmnShape.isIsMessageVisible()) {
				bpmnShape.setIsMessageVisible(true);
				peService.setPropertyValue(context.getPictogramElement(), MESSAGE_VISIBLE, Boolean.toString(true));
			}
		}

		ChoreographyUtil.drawMessageLinks(choreographyContainer);

		String choreoIds = ChoreographyUtil.getMessageRefIds(choreography);
		peService.setPropertyValue(choreographyContainer, MESSAGE_REF_IDS, choreoIds);
		return false;
	}
}