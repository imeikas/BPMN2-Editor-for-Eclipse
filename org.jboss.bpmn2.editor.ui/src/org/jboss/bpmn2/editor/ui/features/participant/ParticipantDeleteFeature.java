/******************************************************************************* 
 * Copyright+ (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.bpmn2.editor.ui.features.participant;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.jboss.bpmn2.editor.ui.features.AbstractDefaultDeleteFeature;
import org.jboss.bpmn2.editor.ui.features.choreography.ChoreographyUtil;

public class ParticipantDeleteFeature extends AbstractDefaultDeleteFeature {

	public ParticipantDeleteFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canDelete(IDeleteContext context) {
		if (ChoreographyUtil.isChoreographyParticipantBand(context.getPictogramElement())) {
			return false;
		}
		return super.canDelete(context);
	}

}