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

public interface ChoreographyProperties {

	int PARTICIPANT_BAND_HEIGHT = 25;

	static final String CHOREOGRAPHY_ACTIVITY_PROPERTY = "choreography.activity";
	static final String PARTICIPANT_REF = "choreography.activity.participant.ref";
	static final String PARTICIPANT_REF_ID = "choreography.activity.participant.ref.id";
	static final String PARTICIPANT_REF_NUM = "choreography.activity.participant.ref.number";
	static final String INITIATING_PARTICIPANT_REF = "choreography.activity.initiating.participant.ref";
	static final String TOP_BAND = "choreography.activity.band.top";
	static final String TOP_BAND_TEXT = "choreography.activity.band.top.text";
	static final String BOTTOM_BAND = "choreography.activity.band.bottom";
	static final String BOTTOM_BAND_TEXT = "choreography.activity.band.bottom.text";
	static final String BODY_BAND = "choreography.activity.band.body";
	static final String BODY_BAND_TEXT = "choreography.activity.band.body.text";
	static final String BODY_LINE_LEFT = "choreography.activity.band.body.line.left";
	static final String BODY_LINE_RIGHT = "choreography.activity.band.body.line.right";
}