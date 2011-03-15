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

import static org.jboss.bpmn2.editor.core.utils.GraphicsUtil.EVENT_SIZE;

import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.jboss.bpmn2.editor.core.di.DIUtils;

public class BoundaryEventLayoutFeature extends AbstractLayoutFeature {

	public BoundaryEventLayoutFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		return true;
	}

	@Override
	public boolean layout(ILayoutContext context) {
		PictogramElement element = context.getPictogramElement();
		GraphicsAlgorithm ga = element.getGraphicsAlgorithm();

		ContainerShape parentContainer = (ContainerShape) element.eContainer();
		GraphicsAlgorithm parentGa = parentContainer.getGraphicsAlgorithm();

		int y = parentGa.getHeight() - EVENT_SIZE;

		DIUtils.updateDIShape(getDiagram(), element, BoundaryEvent.class);

		if (ga.getY() != y) {
			Graphiti.getGaService().setLocation(ga, ga.getX(), y);
			return true;
		}

		return false;
	}
}