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
package org.eclipse.bpmn2.modeler.core.features.artifact;

import java.io.IOException;

import org.eclipse.bpmn2.Artifact;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;

public abstract class AbstractCreateArtifactFeature extends AbstractCreateFeature {

	public AbstractCreateArtifactFeature(IFeatureProvider fp, String name, String description) {
		super(fp, name, description);
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context) && FeatureSupport.isTargetLaneOnTop(context);
		boolean intoParticipant = FeatureSupport.isTargetParticipant(context);
		return intoDiagram || intoLane || intoParticipant;
	}

	@Override
	public Object[] create(ICreateContext context) {
		Artifact artifact = null;
		try {
			ModelHandler handler = ModelHandler.getInstance(getDiagram());
			artifact = createArtifact(context);
			handler.addArtifact(FeatureSupport.getTargetParticipant(context, handler), artifact);
		} catch (IOException e) {
			Activator.logError(e);
		}
		addGraphicalRepresentation(context, artifact);
		return new Object[] { artifact };
	}

	public abstract Artifact createArtifact(ICreateContext context);

	public abstract String getStencilImageId();

	@Override
	public String getCreateImageId() {
		return getStencilImageId();
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId();
	}
}