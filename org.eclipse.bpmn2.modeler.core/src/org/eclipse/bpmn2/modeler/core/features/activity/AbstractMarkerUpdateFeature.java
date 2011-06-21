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
package org.eclipse.bpmn2.modeler.core.features.activity;

import java.util.Iterator;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;

public abstract class AbstractMarkerUpdateFeature extends AbstractUpdateFeature {

	public AbstractMarkerUpdateFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    public boolean canUpdate(IUpdateContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getPictogramElement());
		return bo != null && bo instanceof Activity && context.getPictogramElement() instanceof ContainerShape;
    }

	@Override
    public IReason updateNeeded(IUpdateContext context) {
		IPeService peService = Graphiti.getPeService();
		PictogramElement element = context.getPictogramElement();
		String property = peService.getPropertyValue(element, getPropertyKey());
		if(property == null) {
			return Reason.createFalseReason();
		}
		Activity activity = (Activity) getBusinessObjectForPictogramElement(context.getPictogramElement());
		boolean changed = isPropertyChanged(activity, property);
		return changed ? Reason.createTrueReason() : Reason.createFalseReason();
    }

	@Override
    public boolean update(IUpdateContext context) {
		IPeService peService = Graphiti.getPeService();
		ContainerShape container = (ContainerShape) context.getPictogramElement();
		Activity activity = (Activity) getBusinessObjectForPictogramElement(context.getPictogramElement());

		ContainerShape markerContainer = null;
		Iterator<Shape> iterator = peService.getAllContainedShapes(container).iterator();
		while (iterator.hasNext()) {
			Shape shape = (Shape) iterator.next();
			String property = peService.getPropertyValue(shape, GraphicsUtil.ACTIVITY_MARKER_CONTAINER);
			if(property != null && new Boolean(property)) {
				markerContainer = (ContainerShape) shape;
				break;
			}
		}
		
		doUpdate(activity, markerContainer);
		peService.setPropertyValue(container, getPropertyKey(), convertPropertyToString(activity));
		return true;
    }
	
	abstract String getPropertyKey();
	
	abstract boolean isPropertyChanged(Activity activity, String propertyValue);

	abstract void doUpdate(Activity activity, ContainerShape markerContainer);
	
	abstract String convertPropertyToString(Activity activity);
}
