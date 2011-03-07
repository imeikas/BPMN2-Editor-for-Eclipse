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
package org.jboss.bpmn2.editor.ui.features.choreography;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.ChoreographyTask;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;
import org.jboss.bpmn2.editor.core.features.DefaultBPMNResizeFeature;
import org.jboss.bpmn2.editor.core.features.DefaultBpmnMoveFeature;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;
import org.jboss.bpmn2.editor.core.features.MultiUpdateFeature;
import org.jboss.bpmn2.editor.core.features.choreography.AddChoreographyTaskFeature;
import org.jboss.bpmn2.editor.core.features.choreography.LayoutChoreographyTaskFeature;
import org.jboss.bpmn2.editor.core.features.choreography.UpdateChoreographyNameFeature;
import org.jboss.bpmn2.editor.core.features.choreography.UpdateChoreographyParticipantRefsFeature;
import org.jboss.bpmn2.editor.core.features.choreography.UpdateInitiatingParticipantFeature;
import org.jboss.bpmn2.editor.ui.ImageProvider;

public class ChoreographyTaskFeatureContainer implements FeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof ChoreographyTask;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateChoreographyTaskFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddChoreographyTaskFeature(fp);
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return new LayoutChoreographyTaskFeature(fp);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature updateFeature = new MultiUpdateFeature(fp);
		updateFeature.addUpdateFeature(new UpdateChoreographyNameFeature(fp));
		updateFeature.addUpdateFeature(new UpdateChoreographyParticipantRefsFeature(fp));
		updateFeature.addUpdateFeature(new UpdateInitiatingParticipantFeature(fp));
		return updateFeature;
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
		return new DefaultBpmnMoveFeature(fp);
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
		return new DefaultBPMNResizeFeature(fp);
	}

	public static class CreateChoreographyTaskFeature extends AbstractCreateFlowElementFeature<ChoreographyTask> {

		public CreateChoreographyTaskFeature(IFeatureProvider fp) {
			super(fp, "Choreography Task", "Represents interactions between two participants");
		}

		@Override
		protected ChoreographyTask createFlowElement(ICreateContext context) {
			ChoreographyTask task = ModelHandler.FACTORY.createChoreographyTask();
			task.setName("Choreography Task");
			return task;
		}

		@Override
		public String getCreateImageId() {
			return ImageProvider.IMG_16_CHOREOGRAPHY_TASK;
		}

		@Override
		public String getCreateLargeImageId() {
			return ImageProvider.IMG_16_CHOREOGRAPHY_TASK;
		}
	}

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider context) {
		// TODO Auto-generated method stub
		return null;
	}
}