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

import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.jboss.bpmn2.editor.core.features.DefaultBPMNResizeFeature;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;
import org.jboss.bpmn2.editor.core.features.MoveFlowNodeFeature;
import org.jboss.bpmn2.editor.core.features.MultiUpdateFeature;
import org.jboss.bpmn2.editor.core.features.activity.ActivityCompensateMarkerUpdateFeature;
import org.jboss.bpmn2.editor.core.features.activity.ActivityLoopAndMultiInstanceMarkerUpdateFeature;

public abstract class AbstractActivityFeatureContainer implements FeatureContainer {

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
		return new MoveFlowNodeFeature(fp);
	}

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider fp) {
		return new ActivityDefaultDeleteFeature(fp);
	}
}