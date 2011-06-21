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
package org.eclipse.bpmn2.modeler.ui.features.activity;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.modeler.core.features.BaseElementFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.features.DefaultBPMNResizeFeature;
import org.eclipse.bpmn2.modeler.core.features.MultiUpdateFeature;
import org.eclipse.bpmn2.modeler.core.features.activity.ActivityCompensateMarkerUpdateFeature;
import org.eclipse.bpmn2.modeler.core.features.activity.ActivityLoopAndMultiInstanceMarkerUpdateFeature;
import org.eclipse.bpmn2.modeler.core.features.activity.ActivityMoveFeature;
import org.eclipse.bpmn2.modeler.core.features.event.AbstractBoundaryEventOperation;
import org.eclipse.bpmn2.modeler.ui.features.AbstractDefaultDeleteFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.impl.DeleteContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

public abstract class AbstractActivityFeatureContainer extends BaseElementFeatureContainer {

	@Override
	public MultiUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		ActivityCompensateMarkerUpdateFeature compensateMarkerUpdateFeature = new ActivityCompensateMarkerUpdateFeature(
				fp);
		ActivityLoopAndMultiInstanceMarkerUpdateFeature loopAndMultiInstanceUpdateFeature = new ActivityLoopAndMultiInstanceMarkerUpdateFeature(
				fp);
		MultiUpdateFeature multiUpdate = new MultiUpdateFeature(fp);
		multiUpdate.addUpdateFeature(compensateMarkerUpdateFeature);
		multiUpdate.addUpdateFeature(loopAndMultiInstanceUpdateFeature);
		return multiUpdate;
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
		return new DefaultBPMNResizeFeature(fp);
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
		return new ActivityMoveFeature(fp);
	}

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider fp) {
		return new AbstractDefaultDeleteFeature(fp) {
			@Override
			public void delete(final IDeleteContext context) {
				Activity activity = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(),
						Activity.class);
				new AbstractBoundaryEventOperation() {
					@Override
					protected void doWorkInternal(ContainerShape container) {
						IDeleteContext delete = new DeleteContext(container);
						getFeatureProvider().getDeleteFeature(delete).delete(delete);
					}
				}.doWork(activity, getDiagram());
				super.delete(context);
			}
		};
	}
}