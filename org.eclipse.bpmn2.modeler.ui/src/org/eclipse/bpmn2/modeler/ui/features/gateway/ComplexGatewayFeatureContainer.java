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
package org.eclipse.bpmn2.modeler.ui.features.gateway;

import org.eclipse.bpmn2.ComplexGateway;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.gateway.AbstractCreateGatewayFeature;
import org.eclipse.bpmn2.modeler.core.features.gateway.DefaultAddGatewayFeature;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil.Asterisk;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

public class ComplexGatewayFeatureContainer extends AbstractGatewayFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof ComplexGateway;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateComplexGatewayFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new DefaultAddGatewayFeature(fp) {
			@Override
			protected void decorateGateway(ContainerShape container) {
				Asterisk asterisk = GraphicsUtil.createGatewayAsterisk(container);
				asterisk.horizontal.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				asterisk.vertical.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				asterisk.diagonalAsc.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				asterisk.diagonalDesc.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			}
		};
	}

	public class CreateComplexGatewayFeature extends AbstractCreateGatewayFeature {

		public CreateComplexGatewayFeature(IFeatureProvider fp) {
			super(fp, "Complex Gateway", "Used for modeling complex synchronization behavior");
		}

		@Override
		protected ComplexGateway createFlowElement(ICreateContext context) {
			return ModelHandler.FACTORY.createComplexGateway();
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_COMPLEX_GATEWAY;
		}
	}
}