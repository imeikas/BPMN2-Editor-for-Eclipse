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
import org.eclipse.dd.dc.DcFactory;
import org.eclipse.dd.dc.Point;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddBendpointContext;
import org.eclipse.graphiti.features.impl.DefaultAddBendpointFeature;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class AddBendpointFeature extends DefaultAddBendpointFeature {

	public AddBendpointFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public void addBendpoint(IAddBendpointContext context) {
		super.addBendpoint(context);
		try {
			FreeFormConnection connection = context.getConnection();
			BaseElement element = (BaseElement) BusinessObjectUtil.getFirstElementOfType(connection, BaseElement.class);
			ModelHandler modelHandler = ModelHandlerLocator.getModelHandler(getDiagram().eResource());

			Point p = DcFactory.eINSTANCE.createPoint();
			p.setX(context.getX());
			p.setY(context.getY());
			
			BPMNEdge edge = (BPMNEdge) modelHandler.findDIElement(getDiagram(), element);
			edge.getWaypoint().add(context.getBendpointIndex() + 1, p);
		} catch (Exception e) {
			Activator.logError(e);
		}
	}
}