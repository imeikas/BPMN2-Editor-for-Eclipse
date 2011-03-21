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
package org.jboss.bpmn2.editor.ui.features.activity.task;

import org.eclipse.bpmn2.ReceiveTask;
import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.activity.task.AbstractCreateTaskFeature;
import org.jboss.bpmn2.editor.core.features.activity.task.AddTaskFeature;
import org.jboss.bpmn2.editor.core.utils.GraphicsUtil;
import org.jboss.bpmn2.editor.ui.ImageProvider;

public class ReceiveTaskFeatureContainer extends AbstractTaskFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof ReceiveTask;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateReceiveTaskFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddTaskFeature(fp) {
			@Override
			protected void decorateActivityRectangle(RoundedRectangle rect) {
				IGaService service = Graphiti.getGaService();
				Image img = service.createImage(rect, ImageProvider.IMG_16_RECEIVE_TASK);
				service.setLocationAndSize(img, 2, 2, GraphicsUtil.TASK_IMAGE_SIZE, GraphicsUtil.TASK_IMAGE_SIZE);
			}
		};
	}

	public static class CreateReceiveTaskFeature extends AbstractCreateTaskFeature {

		public CreateReceiveTaskFeature(IFeatureProvider fp) {
			super(fp, "Receive Task", "Task that is completed when a message arrives");
		}

		@Override
		protected Task createFlowElement(ICreateContext context) {
			ReceiveTask task = ModelHandler.FACTORY.createReceiveTask();
			task.setName("Receive Task");
			task.setImplementation("##unspecified");
			return task;
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_RECEIVE_TASK;
		}
	}
}