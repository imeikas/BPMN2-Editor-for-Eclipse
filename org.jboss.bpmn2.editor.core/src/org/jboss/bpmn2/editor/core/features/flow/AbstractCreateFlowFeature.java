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
package org.jboss.bpmn2.editor.core.features.flow;

import java.io.IOException;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.impl.AbstractCreateConnectionFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.utils.FeatureSupport;

public abstract class AbstractCreateFlowFeature<A extends EObject, B extends EObject> extends
        AbstractCreateConnectionFeature {

	public AbstractCreateFlowFeature(IFeatureProvider fp, String name, String description) {
		super(fp, name, description);
	}

	@Override
	public boolean canCreate(ICreateConnectionContext context) {
		A source = getSourceBo(context);
		B target = getTargetBo(context);
		return source != null && target != null;
	}

	@Override
	public Connection create(ICreateConnectionContext context) {
		try {
			A source = getSourceBo(context);
			B target = getTargetBo(context);
			ModelHandler mh = FeatureSupport.getModelHanderInstance(getDiagram());
			AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(),
			        context.getTargetAnchor());
			BaseElement flow = createFlow(mh, source, target);
			flow.setId(EcoreUtil.generateUUID());
			addContext.setNewObject(flow);
			return (Connection) getFeatureProvider().addIfPossible(addContext);
		} catch (IOException e) {
			Activator.logError(e);
		}
		return null;
	}

	@Override
	public boolean canStartConnection(ICreateConnectionContext context) {
		return getSourceBo(context) != null;
	}

	@Override
	public String getCreateImageId() {
		return getStencilImageId();
	}

	@Override
	public String getCreateLargeImageId() {
		return getStencilImageId();
	}

	protected abstract String getStencilImageId();

	protected abstract BaseElement createFlow(ModelHandler mh, A source, B target);

	@SuppressWarnings("unchecked")
	protected A getSourceBo(ICreateConnectionContext context) {
		if (context.getSourceAnchor() != null) {
			return (A) BusinessObjectUtil
			        .getFirstElementOfType(context.getSourceAnchor().getParent(), getSourceClass());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected B getTargetBo(ICreateConnectionContext context) {
		if (context.getTargetAnchor() != null) {
			return (B) BusinessObjectUtil
			        .getFirstElementOfType(context.getTargetAnchor().getParent(), getTargetClass());
		}
		return null;
	}

	protected abstract Class<A> getSourceClass();

	protected abstract Class<B> getTargetClass();
}