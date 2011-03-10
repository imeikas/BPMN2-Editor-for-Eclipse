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
import org.eclipse.dd.dc.Bounds;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;
import org.eclipse.graphiti.services.Graphiti;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil;

public class DefaultBpmnMoveFeature extends DefaultMoveShapeFeature {

	public DefaultBpmnMoveFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		Object[] node = getAllBusinessObjectsForPictogramElement(context.getShape());
		for (Object object : node) {
			if (object instanceof BPMNShape) {
				BPMNShape shape = (BPMNShape) object;
				ILocation loc = Graphiti.getLayoutService().getLocationRelativeToDiagram(context.getShape());
				Bounds bounds = shape.getBounds();
				bounds.setX(loc.getX());
				bounds.setY(loc.getY());
				AnchorUtil.reConnect(shape, getDiagram());
			}
		}
	}
}