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
package org.jboss.bpmn2.editor.ui.features.gateway;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.gateway.AbstractCreateGatewayFeature;
import org.jboss.bpmn2.editor.core.features.gateway.DefaultAddGatewayFeature;
import org.jboss.bpmn2.editor.core.utils.GraphicsUtil;
import org.jboss.bpmn2.editor.core.utils.GraphicsUtil.Cross;
import org.jboss.bpmn2.editor.core.utils.StyleUtil;
import org.jboss.bpmn2.editor.ui.ImageProvider;

public class ParallelGatewayFeatureContainer extends AbstractGatewayFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof ParallelGateway;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateParallelGatewayFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new DefaultAddGatewayFeature(fp) {
			@Override
			protected void decorateGateway(ContainerShape container) {
				Cross cross = GraphicsUtil.createGatewayCross(container);
				cross.vertical.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				cross.horizontal.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			}
		};
	}

	public static class CreateParallelGatewayFeature extends AbstractCreateGatewayFeature {

		public CreateParallelGatewayFeature(IFeatureProvider fp) {
			super(fp, "Parallel Gateway", "Used to combine or create parallel flows");
		}

		@Override
		protected Gateway createFlowElement(ICreateContext context) {
			return ModelHandler.FACTORY.createParallelGateway();
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_PARALLEL_GATEWAY;
		}
	}
}
