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
package org.eclipse.bpmn2.modeler.extras;

import java.util.ArrayList;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.activity.task.AddTaskFeature;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.CustomTaskFeatureContainer;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

public class EmailTaskFeatureContainer extends CustomTaskFeatureContainer {

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateTaskFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddTaskFeature(fp) {

			@Override
			protected void decorateActivityRectangle(RoundedRectangle rect) {
				IGaService service = Graphiti.getGaService();
				Image img = service.createImage(rect, ImageProvider.IMG_16_SEND_TASK);
				service.setLocationAndSize(img, 2, 2, GraphicsUtil.TASK_IMAGE_SIZE, GraphicsUtil.TASK_IMAGE_SIZE);
			}
		};
	}

	public class CreateTaskFeature extends CreateCustomTaskFeature {

		public CreateTaskFeature(IFeatureProvider fp) {
			super(fp, "Email Task", "Create Email Task");
		}

		protected Task createFlowElement(ICreateContext context) {
			Task task = ModelHandler.FACTORY.createTask();
			task.setName("Email Task");
			ArrayList<EStructuralFeature> attributes = Bpmn2Preferences
					.getAttributes(task.eClass());
			for (EStructuralFeature eStructuralFeature : attributes) {
				if (eStructuralFeature.getName().equals("taskName"))
					task.eSet(eStructuralFeature, "email");
			}
			return task;
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_ACTION;
		}
	}
}
