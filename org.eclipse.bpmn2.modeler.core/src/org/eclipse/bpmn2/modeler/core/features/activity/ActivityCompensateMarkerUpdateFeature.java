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

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil.Compensation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

public class ActivityCompensateMarkerUpdateFeature extends AbstractMarkerUpdateFeature {
	
	public static String IS_COMPENSATE_PROPERTY = "marker.compensate";
	
	public ActivityCompensateMarkerUpdateFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    String getPropertyKey() {
	    return IS_COMPENSATE_PROPERTY;
    }

	@Override
	boolean isPropertyChanged(Activity activity, String propertyValue) {
		return activity.isIsForCompensation() != new Boolean(propertyValue);
	}
	
	@Override
	void doUpdate(Activity activity, ContainerShape markerContainer) {
		if (activity.isIsForCompensation()) {
			Compensation compensation = GraphicsUtil.createActivityMarkerCompensate(markerContainer);
			compensation.arrow1.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			compensation.arrow2.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		} else {
			GraphicsUtil.clearActivityMarker(markerContainer, GraphicsUtil.ACTIVITY_MARKER_COMPENSATE);
		}
	}
	
	@Override
    String convertPropertyToString(Activity activity) {
	    return Boolean.toString(activity.isIsForCompensation());
    }
}