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

import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.TEXT_H;

import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class ChoreographyLayoutFeature extends AbstractLayoutFeature {

	protected IPeService peService = Graphiti.getPeService();
	protected IGaService gaService = Graphiti.getGaService();

	public ChoreographyLayoutFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		return BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), ChoreographyActivity.class) != null;
	}

	@Override
	public boolean layout(ILayoutContext context) {
		ContainerShape choreographyContainer = (ContainerShape) context.getPictogramElement();
		GraphicsAlgorithm parentGa = choreographyContainer.getGraphicsAlgorithm();

		int newWidth = parentGa.getWidth();
		int newHeight = parentGa.getHeight();

		for (Shape s : peService.getAllContainedShapes(choreographyContainer)) {
			String property = peService.getPropertyValue(s, ChoreographyProperties.CHOREOGRAPHY_NAME);
			if (property != null && new Boolean(property)) {
				GraphicsAlgorithm ga = s.getGraphicsAlgorithm();
				setTextLocation(choreographyContainer, (Text) ga, newWidth, newHeight);
			}
			property = peService.getPropertyValue(s, ChoreographyProperties.CALL_CHOREO_BORDER);
			if (property != null && new Boolean(property)) {
				GraphicsAlgorithm ga = s.getGraphicsAlgorithm();
				gaService.setSize(ga, newWidth, newHeight);
				peService.sendToFront(s);
			}
		}

		return true;
	}

	protected void setTextLocation(ContainerShape choreographyContainer, Text text, int w, int h) {
		int y = (h / 2) - (TEXT_H / 2);
		gaService.setLocationAndSize(text, 0, y, w, TEXT_H);
	}
}