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

import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.CHOREOGRAPHY_ACTIVITY_PROPERTY;

import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.utils.StyleUtil;

public class CallChoreographyAddFeature extends AbstractChoreographyAddFeature {

	public CallChoreographyAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return super.canAdd(context)
				|| BusinessObjectUtil.containsElementOfType(context.getTargetContainer(), FlowElementsContainer.class);
	}

	@Override
	protected void decorate(ContainerShape containerShape, int w, int h, int bandHeight) {
		// Shape shape = FeatureSupport.getShape(containerShape, CHOREOGRAPHY_ACTIVITY_PROPERTY, TOP_BAND);
		// shape.getGraphicsAlgorithm().setLineWidth(3);
		// shape = FeatureSupport.getShape(containerShape, CHOREOGRAPHY_ACTIVITY_PROPERTY, BOTTOM_BAND);
		// shape.getGraphicsAlgorithm().setLineWidth(3);
		// addLine(containerShape, BODY_LINE_LEFT, new int[] { 0, 5, 0, h - 5 });
		// addLine(containerShape, BODY_LINE_RIGHT, new int[] { w - 3, 5, w - 3, h - 5 });
	}

	private void addLine(ContainerShape containerShape, String propertyValue, int[] xy) {
		Shape shape = peService.createShape(containerShape, false);
		peService.setPropertyValue(shape, CHOREOGRAPHY_ACTIVITY_PROPERTY, propertyValue);
		Polyline lineLeft = gaService.createPolyline(shape, xy);
		lineLeft.setLineWidth(3);
		lineLeft.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
	}
}