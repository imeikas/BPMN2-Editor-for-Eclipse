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
package org.eclipse.bpmn2.modeler.ui.features.activity.subprocess;

import java.util.Collection;

import org.eclipse.bpmn2.SubProcess;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.AbstractDrillDownFeature;

// NOT USED YET
public class SubProcessDrilldownFeature extends AbstractDrillDownFeature {

	public SubProcessDrilldownFeature(IFeatureProvider fp) {
	    super(fp);
    }
	
	@Override
	public String getName() {
	    return "Open sub-process";
	}
	
	@Override
	public String getDescription() {
	    return "Shows sub-process contents";
	}
	
	@Override
	public boolean canExecute(ICustomContext context) {
		PictogramElement[] elements = context.getPictogramElements();
		if(elements != null && elements.length == 1) {
			Object o = getBusinessObjectForPictogramElement(elements[0]);
			if( o instanceof SubProcess ) {
				return super.canExecute(context);
			}
		}
	    return false;
	}
	
	@Override
    protected Collection<Diagram> getDiagrams() {
	    return null;
    }
}