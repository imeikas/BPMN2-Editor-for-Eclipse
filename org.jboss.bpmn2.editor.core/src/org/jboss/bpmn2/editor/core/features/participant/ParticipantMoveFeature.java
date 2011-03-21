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
package org.jboss.bpmn2.editor.core.features.participant;

import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.features.DefaultBpmnMoveFeature;

public class ParticipantMoveFeature extends DefaultBpmnMoveFeature {

	public ParticipantMoveFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		if (isChoreographyParticipantBand(context)) {
			return false;
		}
		return super.canMoveShape(context);
	}

	private boolean isChoreographyParticipantBand(IMoveShapeContext context) {
		EObject container = context.getShape().eContainer();
		if (container instanceof PictogramElement) {
			PictogramElement containerElem = (PictogramElement) container;
			if (BusinessObjectUtil.containsElementOfType(containerElem, ChoreographyActivity.class)) {
				return true;
			}
		}
		return false;
	}
}