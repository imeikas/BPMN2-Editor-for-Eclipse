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
package org.eclipse.bpmn2.modeler.core.features.choreography;

public interface ChoreographyProperties {

	int PARTICIPANT_BAND_HEIGHT = 20;
	int ENV_W = 30;
	int ENV_H = 18;
	int ENVELOPE_HEIGHT_MODIFIER = 30;
	int R = 10;
	int TEXT_H = 15;
	int MARKER_H = 20;

	String CHOREOGRAPHY_ACTIVITY_PROPERTY = "choreography.activity";
	String PARTICIPANT_REF = "choreography.activity.participant.ref";
	String PARTICIPANT_REF_ID = "choreography.activity.participant.ref.id";
	String PARTICIPANT_REF_IDS = "choreography.activity.participant.ref.ids";
	String INITIATING_PARTICIPANT_REF = "choreography.activity.initiating.participant.ref";
	String MESSAGE_VISIBLE = "choreography.activity.band.message.visible";
	String BAND = "choreography.activity.band";
	String MESSAGE_LINK = "choreography.messageLink";
	String CHOREOGRAPHY_NAME = "choreography.name";
	String CALL_CHOREO_BORDER = "call.choreography.border";
	String CHOREOGRAPHY_MARKER = "choreography.marker";
	String CHOREOGRAPHY_MARKER_SHAPE = "choreography.marker.shape";
	String MESSAGE_REF_IDS = "choreography.message.ref.ids";
}