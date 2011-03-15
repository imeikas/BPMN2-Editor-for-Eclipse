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

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.features.MoveFlowNodeFeature;

public class BoundaryEventMoveFeature extends MoveFlowNodeFeature {

	public BoundaryEventMoveFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		super.postMoveShape(context);
		Object property = context.getProperty("activity.move");
		if (property != null && (Boolean) property) {
			ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
			IGaService gaService = Graphiti.getGaService();
			GraphicsAlgorithm ga = containerShape.getGraphicsAlgorithm();
			gaService.setLocation(ga, ga.getX() + context.getDeltaX(), ga.getY() + context.getDeltaY());
		}
	}
}