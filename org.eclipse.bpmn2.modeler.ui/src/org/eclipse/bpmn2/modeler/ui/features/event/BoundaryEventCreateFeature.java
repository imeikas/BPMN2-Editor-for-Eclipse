/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.features.event;

import java.io.IOException;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class BoundaryEventCreateFeature extends AbstractCreateFeature {

	public BoundaryEventCreateFeature(IFeatureProvider fp) {
		super(fp, "Boundary Event", "Adds boundary event to activity, defaults to interrupting");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		Object o = getBusinessObjectForPictogramElement(context.getTargetContainer());
		if (o == null || !(o instanceof Activity)) {
			return false;
		}

		GraphicsAlgorithm ga = context.getTargetContainer().getGraphicsAlgorithm();
		return BoundaryEventPositionHelper.canCreateEventAt(context, ga, 10);
	}

	@Override
	public Object[] create(ICreateContext context) {
		BoundaryEvent event = null;
		try {
			Activity activity = (Activity) getBusinessObjectForPictogramElement(context.getTargetContainer());
			ModelHandler handler = ModelHandler.getInstance(getDiagram());
			event = ModelHandler.FACTORY.createBoundaryEvent();
//			event.setId(EcoreUtil.generateUUID());
			event.setAttachedToRef(activity);
			event.setName("Boundary event");
			event.setCancelActivity(true); // by default is interrupting
			Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
			if (bo instanceof FlowElementsContainer) {
				bo = getBusinessObjectForPictogramElement((PictogramElement) context.getTargetContainer().eContainer());
			}
			handler.addFlowElement(bo, event);
		} catch (IOException e) {
			Activator.logError(e);
		}
		addGraphicalRepresentation(context, event);
		ModelUtil.setID(event);
		return new Object[] { event };
	}

	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_BOUNDARY_EVENT;
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId();
	}
}