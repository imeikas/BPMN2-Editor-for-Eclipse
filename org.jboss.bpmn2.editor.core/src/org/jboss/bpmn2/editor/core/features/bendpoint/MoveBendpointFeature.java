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
package org.jboss.bpmn2.editor.core.features.bendpoint;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.dd.dc.Point;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveBendpointContext;
import org.eclipse.graphiti.features.impl.DefaultMoveBendpointFeature;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class MoveBendpointFeature extends DefaultMoveBendpointFeature {

	public MoveBendpointFeature(IFeatureProvider fp) {
	    super(fp);
    }
	
	@Override
	public boolean moveBendpoint(IMoveBendpointContext context) {
		boolean moved = super.moveBendpoint(context); 
		try {
			FreeFormConnection connection = context.getConnection();
			BaseElement element = (BaseElement) BusinessObjectUtil.getFirstElementOfType(connection, BaseElement.class);
			ModelHandler modelHandler = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
			BPMNEdge edge = (BPMNEdge) modelHandler.findDIElement(getDiagram(), element);
			Point p = edge.getWaypoint().get(context.getBendpointIndex() + 1);
			p.setX(context.getX());
			p.setY(context.getY());
		} catch (Exception e) {
			Activator.logError(e);
		}
		return moved;
	}
}