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
package org.eclipse.bpmn2.modeler.core.features.data;

import java.io.IOException;

import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;

public abstract class AbstractCreateRootElementFeature extends AbstractCreateFeature {

	public AbstractCreateRootElementFeature(IFeatureProvider fp, String name, String description) {
	    super(fp, name, description);
    }

	@Override
    public boolean canCreate(ICreateContext context) {
	    return true;
    }

	@Override
    public Object[] create(ICreateContext context) {
		RootElement element = null;
		
		try {
			ModelHandler handler = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
			element = createRootElement();
//			element.setId(EcoreUtil.generateUUID());
			handler.addRootElement(element);
		} catch (IOException e) {
			Activator.logError(e);
		}

		addGraphicalRepresentation(context, element);
		ModelUtil.setID(element);
		return new Object[] { element };
    }
	
	public abstract RootElement createRootElement();
	
	public abstract String getStencilImageId();
	
	@Override
	public String getCreateImageId() {
	    return getStencilImageId();
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getStencilImageId();
	}
}
