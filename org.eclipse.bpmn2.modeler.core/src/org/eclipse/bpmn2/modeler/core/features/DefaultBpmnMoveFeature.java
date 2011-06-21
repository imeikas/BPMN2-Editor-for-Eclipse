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
package org.eclipse.bpmn2.modeler.core.features;

import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.impl.DefaultMoveShapeFeature;

public class DefaultBpmnMoveFeature extends DefaultMoveShapeFeature {

	public DefaultBpmnMoveFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		DIUtils.updateDIShape(context.getPictogramElement());

		Object[] node = getAllBusinessObjectsForPictogramElement(context.getShape());
		for (Object object : node) {
			if (object instanceof BPMNShape) {
				AnchorUtil.reConnect((BPMNShape) object, getDiagram());
			}
		}
	}
}