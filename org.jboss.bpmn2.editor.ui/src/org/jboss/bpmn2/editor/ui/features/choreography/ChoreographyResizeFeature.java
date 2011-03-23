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
package org.jboss.bpmn2.editor.ui.features.choreography;

import java.util.List;

import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.features.DefaultBPMNResizeFeature;
import org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties;
import org.jboss.bpmn2.editor.core.utils.Tuple;

public class ChoreographyResizeFeature extends DefaultBPMNResizeFeature {

	public ChoreographyResizeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		try {
			List<BPMNShape> bands = ChoreographyUtil.getParicipantBandBpmnShapes((ContainerShape) context
					.getPictogramElement());
			int h = ChoreographyProperties.TEXT_H; // + ChoreographyProperties.MARKER_H;

			for (BPMNShape shape : bands) {
				h += shape.getBounds().getHeight();
			}

			return context.getHeight() > 0 ? context.getHeight() > h : true;
		} catch (Exception e) {
			Activator.logError(e);
			return true;
		}
	}

	@Override
	public void resizeShape(IResizeShapeContext context) {
		try {
			List<ContainerShape> bands = ChoreographyUtil.getParticipantBandContainerShapes((ContainerShape) context
					.getPictogramElement());
			Tuple<List<ContainerShape>, List<ContainerShape>> topAndBottom = ChoreographyUtil
					.getTopAndBottomBands(bands);
			ChoreographyUtil.resizePartipantBandContainerShapes(context.getWidth(), context.getHeight(),
					topAndBottom.getFirst(), topAndBottom.getSecond(), getDiagram());
		} catch (Exception e) {
			Activator.logError(e);
		}
		super.resizeShape(context);
		ChoreographyUtil.moveChoreographyMessageLinks((ContainerShape) context.getPictogramElement());
	}

}