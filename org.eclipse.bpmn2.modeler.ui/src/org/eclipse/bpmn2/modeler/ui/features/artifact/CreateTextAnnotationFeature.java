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
package org.eclipse.bpmn2.modeler.ui.features.artifact;

import java.io.IOException;

import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;

public class CreateTextAnnotationFeature extends AbstractCreateFeature {

	public CreateTextAnnotationFeature(IFeatureProvider fp) {
		super(fp, "Annotation", "Provide additional information");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context) && FeatureSupport.isTargetLaneOnTop(context);
		return intoDiagram || intoLane;
	}

	@Override
	public Object[] create(ICreateContext context) {
		TextAnnotation ta = null;

		try {
			ModelHandler mh = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
			ta = ModelHandler.FACTORY.createTextAnnotation();
//			ta.setId(EcoreUtil.generateUUID());
			mh.addArtifact(FeatureSupport.getTargetParticipant(context, mh), ta);
			ta.setText("Enter your comment here");
		} catch (IOException e) {
			Activator.logError(e);
		}

		addGraphicalRepresentation(context, ta);
		ModelUtil.setID(ta);

		return new Object[] { ta };
	}

	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_TEXT_ANNOTATION;
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); // FIXME
	}
}
