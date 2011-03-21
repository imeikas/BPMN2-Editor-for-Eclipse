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
package org.jboss.bpmn2.editor.core.features;

import java.io.IOException;

import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowElementsContainer;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.utils.FeatureSupport;

public abstract class AbstractCreateFlowElementFeature<T extends FlowElement> extends AbstractCreateFeature {

	public AbstractCreateFlowElementFeature(IFeatureProvider fp, String name, String description) {
		super(fp, name, description);
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context) && FeatureSupport.isTargetLaneOnTop(context);
		boolean intoParticipant = FeatureSupport.isTargetParticipant(context);
		boolean intoFlowElementContainer = BusinessObjectUtil.containsElementOfType(context.getTargetContainer(),
				FlowElementsContainer.class);
		return intoDiagram || intoLane || intoParticipant || intoFlowElementContainer;
	}

	@Override
	public Object[] create(ICreateContext context) {
		T element = null;
		try {
			ModelHandler handler = ModelHandler.getInstance(getDiagram());
			element = createFlowElement(context);
			element.setId(EcoreUtil.generateUUID());
			if (FeatureSupport.isTargetLane(context) && element instanceof FlowNode) {
				((FlowNode) element).getLanes().add(
						(Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
			}

			handler.addFlowElement(getBusinessObjectForPictogramElement(context.getTargetContainer()), element);
		} catch (IOException e) {
			Activator.logError(e);
		}
		addGraphicalRepresentation(context, element);
		return new Object[] { element };
	}

	protected abstract T createFlowElement(ICreateContext context);
}