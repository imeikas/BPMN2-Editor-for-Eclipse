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
package org.eclipse.bpmn2.modeler.core.features.event.definitions;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.modeler.core.features.event.definitions.EventDefinitionSupport.EventWithDefinitions;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;

public abstract class CreateEventDefinition extends AbstractCreateFeature {

	protected EventDefinitionSupport support = new EventDefinitionSupport();

	public CreateEventDefinition(IFeatureProvider fp, String name, String description) {
		super(fp, name, description);
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
		return bo != null && bo instanceof Event;
	}

	@Override
	public Object[] create(ICreateContext context) {
		Event e = (Event) getBusinessObjectForPictogramElement(context.getTargetContainer());
		EventWithDefinitions event = support.create(e);
		EventDefinition definition = createEventDefinition(context);
		event.getEventDefinitions().add(definition);
		addGraphicalRepresentation(context, definition);
		return new Object[] { definition };
	}

	protected abstract EventDefinition createEventDefinition(ICreateContext context);

	protected abstract String getStencilImageId();

	@Override
	public String getCreateImageId() {
		return getStencilImageId();
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); // FIXME
	}
}