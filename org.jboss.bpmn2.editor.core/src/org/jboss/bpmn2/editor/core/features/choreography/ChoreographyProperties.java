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

	static final String CHOREOGRAPHY_ACTIVITY_PROPERTY = "choreography.task";
	static final String PARTICIPANT_REF = "choreography.task.participant.ref";
	static final String PARTICIPANT_REF_ID = "choreography.task.participant.ref.id";
	static final String PARTICIPANT_REF_NUM = "choreography.task.participant.ref.number";
	static final String INITIATING_PARTICIPANT_REF = "choreography.task.initiating.participant.ref";
	static final String TOP_BAND = "choreography.task.band.top";
	static final String TOP_BAND_TEXT = "choreography.task.band.top.text";
	static final String BOTTOM_BAND = "choreography.task.band.bottom";
	static final String BOTTOM_BAND_TEXT = "choreography.task.band.bottom.text";
	static final String BODY_BAND = "choreography.task.band.body";
	static final String BODY_BAND_TEXT = "choreography.task.band.body.text";

}