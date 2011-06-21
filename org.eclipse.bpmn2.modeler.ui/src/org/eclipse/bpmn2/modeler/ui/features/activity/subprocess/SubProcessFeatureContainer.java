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
package org.eclipse.bpmn2.modeler.ui.features.activity.subprocess;

import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.MultiUpdateFeature;
import org.eclipse.bpmn2.modeler.core.features.activity.subprocess.AbstractCreateSubProcess;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;

public class SubProcessFeatureContainer extends AbstractSubProcessFeatureContainer {

	public static final String TRIGGERED_BY_EVENT = "triggered-by-event-key";

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof SubProcess;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateSubProcessFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddExpandedSubProcessFeature(fp);
	}

	@Override
	public MultiUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature multiUpdate = super.getUpdateFeature(fp);
		UpdateSubProcessFeature updateSubProcessFeature = new UpdateSubProcessFeature(fp);
		multiUpdate.addUpdateFeature(updateSubProcessFeature);
		return multiUpdate;
	}

	public static class CreateSubProcessFeature extends AbstractCreateSubProcess {

		public CreateSubProcessFeature(IFeatureProvider fp) {
			super(fp, "Expanded Sub-Process", "Inner activity");
		}

		@Override
		protected SubProcess createFlowElement(ICreateContext context) {
			SubProcess subProcess = ModelHandler.FACTORY.createSubProcess();
			subProcess.setName("SubProcess");
			subProcess.setTriggeredByEvent(false);
			return subProcess;
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_SUB_PROCESS;
		}
	}
}