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

	int PARTICIPANT_BAND_HEIGHT = 20;
	int ENV_W = 30;
	int ENV_H = 18;
	int ENVELOPE_HEIGHT_MODIFIER = 30;
	int R = 10;

	static final String CHOREOGRAPHY_ACTIVITY_PROPERTY = "choreography.activity";

	static final String PARTICIPANT_REF = "choreography.activity.participant.ref";
	static final String PARTICIPANT_REF_ID = "choreography.activity.participant.ref.id";
	static final String PARTICIPANT_REF_IDS = "choreography.activity.participant.ref.ids";
	static final String INITIATING_PARTICIPANT_REF = "choreography.activity.initiating.participant.ref";

	static final String BAND = "choreography.activity.band";
}