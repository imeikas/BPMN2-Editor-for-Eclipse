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
import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.bpmn2.SubChoreography;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;
import org.jboss.bpmn2.editor.core.features.choreography.AddSubChoreographyFeature;
import org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties;
import org.jboss.bpmn2.editor.core.features.choreography.LayoutChoreographyFeature;
import org.jboss.bpmn2.editor.ui.ImageProvider;

public class SubChoreographyFeatureContainer extends AbstractChoreographyFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof SubChoreography;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateSubChoreographyFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddSubChoreographyFeature(fp) {
			@Override
			protected void hook(ContainerShape containerShape, ChoreographyActivity choreography, int w, int h,
			        int bandHeight) {
				createText(containerShape, choreography.getName(), 0, bandHeight, w, 10,
				        ChoreographyProperties.BODY_BAND_TEXT);
			}
		};
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return new LayoutChoreographyFeature(fp) {
			@Override
			protected void layoutBodyText(GraphicsAlgorithm ga, int w, int h, int bandHeight, int y) {
				gaService.setLocationAndSize(ga, 0, y, w, 15);
			}
		};
	}

	public static class CreateSubChoreographyFeature extends AbstractCreateFlowElementFeature<SubChoreography> {

		public CreateSubChoreographyFeature(IFeatureProvider fp) {
			super(fp, "Expanded Sub-Choreography", "A compound activity that can contain other activities");
		}

		@Override
		protected SubChoreography createFlowElement(ICreateContext context) {
			SubChoreography subChoreography = ModelHandler.FACTORY.createSubChoreography();
			subChoreography.setName("Sub-Choreography");
			return subChoreography;
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