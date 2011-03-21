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
package org.jboss.bpmn2.editor.ui.features.artifact;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.jboss.bpmn2.editor.core.features.DefaultBPMNResizeFeature;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;
import org.jboss.bpmn2.editor.core.features.artifact.AddTextAnnotationFeature;
import org.jboss.bpmn2.editor.core.features.artifact.DirectEditTextAnnotationFeature;
import org.jboss.bpmn2.editor.core.features.artifact.LayoutTextAnnotationFeature;
import org.jboss.bpmn2.editor.core.features.artifact.MoveTextAnnotationFeature;
import org.jboss.bpmn2.editor.core.features.artifact.UpdateTextAnnotationFeature;
import org.jboss.bpmn2.editor.ui.features.AbstractDefaultDeleteFeature;

public class TextAnnotationFeatureContainer implements FeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof TextAnnotation;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateTextAnnotationFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddTextAnnotationFeature(fp);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		return new UpdateTextAnnotationFeature(fp);
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
		return new DirectEditTextAnnotationFeature(fp);
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return new LayoutTextAnnotationFeature(fp);
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
		return new MoveTextAnnotationFeature(fp);
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
		return new DefaultBPMNResizeFeature(fp);
	}

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider fp) {
		return new AbstractDefaultDeleteFeature(fp);
	}

}