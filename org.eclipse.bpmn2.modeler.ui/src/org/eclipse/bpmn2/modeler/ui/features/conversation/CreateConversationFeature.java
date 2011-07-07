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
package org.eclipse.bpmn2.modeler.ui.features.conversation;

import java.io.IOException;

import org.eclipse.bpmn2.Conversation;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;

public class CreateConversationFeature extends AbstractCreateFeature {

	public CreateConversationFeature(IFeatureProvider fp) {
		super(fp, "Conversation", "A logical grouping of Message exchanges");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		return context.getTargetContainer().equals(getDiagram());
	}

	@Override
	public Object[] create(ICreateContext context) {
		Conversation c = null;
		try {
			ModelHandler handler = ModelHandler.getInstance(getDiagram());
			c = ModelHandler.FACTORY.createConversation();
//			c.setId(EcoreUtil.generateUUID());
			c.setName("Conversation");
			handler.addConversationNode(c);
			ModelUtil.setID(c);
		} catch (IOException e) {
			Activator.logError(e);
		}
		addGraphicalRepresentation(context, c);
		return new Object[] { c };
	}

	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_CONVERSATION;
	}

	@Override
	public String getCreateLargeImageId() {
		return ImageProvider.IMG_16_CONVERSATION;
	}
}