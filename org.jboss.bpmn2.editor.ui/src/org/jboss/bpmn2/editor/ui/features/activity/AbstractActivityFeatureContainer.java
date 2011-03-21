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
package org.jboss.bpmn2.editor.ui.features.activity;

import org.eclipse.bpmn2.Activity;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.impl.DeleteContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.jboss.bpmn2.editor.core.features.BaseElementFeatureContainer;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.features.DefaultBPMNResizeFeature;
import org.jboss.bpmn2.editor.core.features.MultiUpdateFeature;
import org.jboss.bpmn2.editor.core.features.activity.ActivityCompensateMarkerUpdateFeature;
import org.jboss.bpmn2.editor.core.features.activity.ActivityLoopAndMultiInstanceMarkerUpdateFeature;
import org.jboss.bpmn2.editor.core.features.activity.ActivityMoveFeature;
import org.jboss.bpmn2.editor.core.features.event.AbstractBoundaryEventOperation;
import org.jboss.bpmn2.editor.ui.features.AbstractDefaultDeleteFeature;

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
				new AbstractBoundaryEventOperation(activity, getDiagram()) {
					@Override
					protected void doWork(ContainerShape container) {
						IDeleteContext delete = new DeleteContext(container);
						getFeatureProvider().getDeleteFeature(delete).delete(delete);
					}
				};
				super.delete(context);
			}
		};
	}
}