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

import org.eclipse.bpmn2.ChoreographyTask;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;
import org.jboss.bpmn2.editor.core.features.choreography.ChoreographyTaskAddFeature;
import org.jboss.bpmn2.editor.ui.ImageProvider;

public class ChoreographyTaskFeatureContainer extends AbstractChoreographyFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof ChoreographyTask;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateChoreographyTaskFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new ChoreographyTaskAddFeature(fp);
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
}