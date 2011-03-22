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

import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.jboss.bpmn2.editor.core.di.DIUtils;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil;

public class DefaultBPMNResizeFeature extends DefaultResizeShapeFeature {

	public DefaultBPMNResizeFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public void resizeShape(IResizeShapeContext context) {
		super.resizeShape(context);
		if (context.getPictogramElement() instanceof Shape) {
			Shape shape = (Shape) context.getPictogramElement();
			AnchorUtil.relocateFixPointAnchors(shape, context.getWidth(), context.getHeight());
			Object[] node = getAllBusinessObjectsForPictogramElement(context.getShape());
			for (Object object : node) {
				if (object instanceof BPMNShape) {
					BPMNShape s = (BPMNShape) object;
					AnchorUtil.reConnect(s, getDiagram());
				}
			}
		}
		DIUtils.updateDIShape(context.getPictogramElement());
	}
}