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
package org.eclipse.bpmn2.modeler.extras;

import java.util.ArrayList;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.Bpmn2Preferences;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.activity.task.AbstractCreateTaskFeature;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.TaskFeatureContainer;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;

public class LogTaskFeatureContainer extends TaskFeatureContainer {

		public ICreateFeature getCreateFeature(IFeatureProvider fp) {
			return new CreateTaskFeature(fp);
		}

		public static class CreateTaskFeature extends AbstractCreateTaskFeature {

			public CreateTaskFeature(IFeatureProvider fp) {
				super(fp, "Log Task", "Create Log Task");
			}

			protected Task createFlowElement(ICreateContext context) {
				Task task = ModelHandler.FACTORY.createTask();
				task.setName("Log Task");
				ArrayList<EStructuralFeature> attributes = Bpmn2Preferences
						.getAttributes(task.eClass());
				for (EStructuralFeature eStructuralFeature : attributes) {
					if (eStructuralFeature.getName().equals("taskName"))
						task.eSet(eStructuralFeature, "log");
				}
				return task;
			}

			@Override
			protected String getStencilImageId() {
				return ImageProvider.IMG_16_ACTION;
			}
	}
}

