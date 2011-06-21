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
package org.eclipse.bpmn2.modeler.core.features.activity;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.features.MoveFlowNodeFeature;
import org.eclipse.bpmn2.modeler.core.features.event.AbstractBoundaryEventOperation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

public class ActivityMoveFeature extends MoveFlowNodeFeature {

	public static final String ACTIVITY_MOVE_PROPERTY = "activity.move";

	public ActivityMoveFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void postMoveShape(final IMoveShapeContext context) {
		super.postMoveShape(context);
		Activity activity = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), Activity.class);
		new AbstractBoundaryEventOperation() {
			@Override
			protected void doWorkInternal(ContainerShape container) {
				GraphicsAlgorithm ga = container.getGraphicsAlgorithm();

				MoveShapeContext newContext = new MoveShapeContext(container);
				newContext.setDeltaX(context.getDeltaX());
				newContext.setDeltaY(context.getDeltaY());
				newContext.setSourceContainer(context.getSourceContainer());
				newContext.setTargetContainer(context.getTargetContainer());
				newContext.setTargetConnection(context.getTargetConnection());
				newContext.setLocation(ga.getX(), ga.getY());
				newContext.putProperty(ACTIVITY_MOVE_PROPERTY, true);

				IMoveShapeFeature moveFeature = getFeatureProvider().getMoveShapeFeature(newContext);
				if (moveFeature.canMoveShape(newContext)) {
					moveFeature.moveShape(newContext);
				}
			}
		}.doWork(activity, getDiagram());
	}
}