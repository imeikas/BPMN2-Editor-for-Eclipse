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
package org.jboss.bpmn2.editor.core.features.gateway;

import org.eclipse.bpmn2.Gateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public abstract class AbstractCreateGatewayFeature extends AbstractCreateFlowElementFeature<Gateway> {

	public AbstractCreateGatewayFeature(IFeatureProvider fp, String name, String description) {
	    super(fp, name, description);
    }

	protected abstract String getStencilImageId();
	
	@Override
	public String getCreateImageId() {
	    return getStencilImageId();
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId();
	}
}
