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
import org.eclipse.bpmn2.modeler.core.features.MultiUpdateFeature;
import org.eclipse.bpmn2.modeler.core.features.UpdateBaseElementNameFeature;
import org.eclipse.bpmn2.modeler.core.features.activity.task.AddTaskFeature;
import org.eclipse.bpmn2.modeler.core.features.activity.task.UpdateTaskFeature;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.CustomTaskFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.flow.SequenceFlowFeatureContainer.UpdateConditionalSequenceFlowFeature;
import org.eclipse.bpmn2.modeler.ui.features.flow.SequenceFlowFeatureContainer.UpdateDefaultSequenceFlowFeature;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IAreaContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.Property;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

public class LogTaskFeatureContainer extends CustomTaskFeatureContainer {

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateTaskFeature(fp);
	}

	@Override
	public MultiUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature multiUpdate = new MultiUpdateFeature(fp);
		multiUpdate.addUpdateFeature(new UpdateTaskFeature(fp) {

			@Override
			public boolean canUpdate(IUpdateContext context) {
				// TODO Auto-generated method stub
				return super.canUpdate(context);
			}

			@Override
			public IReason updateNeeded(IUpdateContext context) {
				// TODO Auto-generated method stub
				return super.updateNeeded(context);
			}

			@Override
			public boolean update(IUpdateContext context) {
				// TODO Auto-generated method stub
				return super.update(context);
			}
			
		});
		return multiUpdate;

	}
	
	public class CreateTaskFeature extends CreateCustomTaskFeature {

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
