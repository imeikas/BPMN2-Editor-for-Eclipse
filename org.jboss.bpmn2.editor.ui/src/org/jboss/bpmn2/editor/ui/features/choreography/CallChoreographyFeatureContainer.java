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
import org.eclipse.bpmn2.CallChoreography;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;
import org.jboss.bpmn2.editor.core.features.choreography.AddChoreographyFeature;
import org.jboss.bpmn2.editor.ui.ImageProvider;

public class CallChoreographyFeatureContainer extends AbstractChoreographyFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof CallChoreography;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateCallChoreographyFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddChoreographyFeature(fp);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		// MultiUpdateFeature updateFeature = new MultiUpdateFeature(fp);
		// updateFeature.addUpdateFeature(new UpdateChoreographyParticipantRefsFeature(fp) {
		// @Override
		// protected boolean showNames() {
		// return false;
		// }
		// });
		// updateFeature.addUpdateFeature(new UpdateInitiatingParticipantFeature(fp));
		// return updateFeature;
		return null;
	}

	public static class CreateCallChoreographyFeature extends AbstractCreateFlowElementFeature<CallChoreography> {

		public CreateCallChoreographyFeature(IFeatureProvider fp) {
			super(fp, "Call Choreography",
					"Identifies a point in the Process where a global Choreography or a Global Choreography Task is used");
		}

		@Override
		protected CallChoreography createFlowElement(ICreateContext context) {
			CallChoreography callChoreography = ModelHandler.FACTORY.createCallChoreography();
			callChoreography.setName("Call Choreography");
			return callChoreography;
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