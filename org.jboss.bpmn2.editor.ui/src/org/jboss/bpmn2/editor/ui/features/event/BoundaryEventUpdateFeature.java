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
package org.jboss.bpmn2.editor.ui.features.event;

import static org.jboss.bpmn2.editor.ui.features.event.BoundaryEventFeatureContainer.BOUNDARY_EVENT_CANCEL;

import java.util.List;

import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.CancelEventDefinition;
import org.eclipse.bpmn2.CompensateEventDefinition;
import org.eclipse.bpmn2.ErrorEventDefinition;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.Property;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.services.Graphiti;

public class BoundaryEventUpdateFeature extends AbstractUpdateFeature {

	public BoundaryEventUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		Property cancelProperty = Graphiti.getPeService().getProperty(context.getPictogramElement(),
		        BOUNDARY_EVENT_CANCEL);
		BoundaryEvent event = (BoundaryEvent) getBusinessObjectForPictogramElement(context.getPictogramElement());
		boolean changed = Boolean.parseBoolean(cancelProperty.getValue()) != event.isCancelActivity();
		IReason reason = changed ? Reason.createTrueReason("Boundary type changed") : Reason.createFalseReason();
		return reason;
	}

	@Override
	public boolean update(IUpdateContext context) {
		BoundaryEvent event = (BoundaryEvent) getBusinessObjectForPictogramElement(context.getPictogramElement());

		boolean canUpdate = true;

		List<EventDefinition> definitions = event.getEventDefinitions();

		if (event.isCancelActivity() == false) {
			for (EventDefinition d : definitions) {
				if (d instanceof ErrorEventDefinition || d instanceof CancelEventDefinition
				        || d instanceof CompensateEventDefinition) {
					canUpdate = false;
					break;
				}
			}
		}

		if (canUpdate == false) {
			return false;
		}

		Graphiti.getPeService().setPropertyValue(context.getPictogramElement(), BOUNDARY_EVENT_CANCEL,
		        Boolean.toString(event.isCancelActivity()));

		Ellipse ellipse = (Ellipse) context.getPictogramElement().getGraphicsAlgorithm();
		Ellipse innerEllipse = (Ellipse) ellipse.getGraphicsAlgorithmChildren().get(0);
		LineStyle lineStyle = event.isCancelActivity() ? LineStyle.SOLID : LineStyle.DASH;

		ellipse.setLineStyle(lineStyle);
		innerEllipse.setLineStyle(lineStyle);

		return true;
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof BoundaryEvent;
	}
}