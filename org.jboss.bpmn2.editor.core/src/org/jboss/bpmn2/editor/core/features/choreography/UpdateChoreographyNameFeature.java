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

import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.BODY_BAND_TEXT;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.CHOREOGRAPHY_ACTIVITY_PROPERTY;

import java.util.Iterator;

import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class UpdateChoreographyNameFeature extends AbstractUpdateFeature {

	private static final IPeService peService = Graphiti.getPeService();

	public UpdateChoreographyNameFeature(IFeatureProvider fp) {
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
		if (activity.getName().equals(getBodyText(context).getValue())) {
			return Reason.createFalseReason();
		} else {
			return Reason.createTrueReason("Name is out of date");
		}
	}

	@Override
	public boolean update(IUpdateContext context) {
		ChoreographyActivity task = (ChoreographyActivity) BusinessObjectUtil.getFirstElementOfType(
		        context.getPictogramElement(), ChoreographyActivity.class);
		getBodyText(context).setValue(task.getName());
		return true;
	}

	private Text getBodyText(IUpdateContext context) {
		Iterator<Shape> iterator = peService.getAllContainedShapes((ContainerShape) context.getPictogramElement())
		        .iterator();
		while (iterator.hasNext()) {
			Shape shape = (Shape) iterator.next();
			String property = peService.getPropertyValue(shape, CHOREOGRAPHY_ACTIVITY_PROPERTY);
			if (property != null && property.equals(BODY_BAND_TEXT)) {
				return (Text) shape.getGraphicsAlgorithm();
			}
		}
		return null;
	}
}