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
package org.eclipse.bpmn2.modeler.core.features;

import static org.eclipse.bpmn2.modeler.core.utils.FeatureSupport.getChildElementOfType;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.AbstractText;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class UpdateBaseElementNameFeature extends AbstractUpdateFeature {

	public static final String TEXT_ELEMENT = "baseelement.text";

	public UpdateBaseElementNameFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		BaseElement element = (BaseElement) BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(),
		        BaseElement.class);
		if (element == null) {
			return false;
		}
		return ModelUtil.hasName(element);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		PictogramElement container = context.getPictogramElement();

		BaseElement element = (BaseElement) BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(),
		        BaseElement.class);

		String elementName = ModelUtil.getName(element);
		Shape textShape = getChildElementOfType(container, TEXT_ELEMENT, Boolean.toString(true), Shape.class);
		if (textShape!=null) {
			String name = ((Text) textShape.getGraphicsAlgorithm()).getValue();
	
			if (elementName != null) {
				return elementName.equals(name) ? Reason.createFalseReason() : Reason.createTrueReason();
			} else if (name != null) {
				return name.equals(elementName) ? Reason.createFalseReason() : Reason.createTrueReason();
			}
		}
		return Reason.createFalseReason();
	}

	@Override
	public boolean update(IUpdateContext context) {
		PictogramElement container = (PictogramElement) context.getPictogramElement();
		BaseElement element = (BaseElement) BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(),
		        BaseElement.class);
		Shape textShape = getChildElementOfType(container, TEXT_ELEMENT, Boolean.toString(true), Shape.class);
		if (textShape!=null) {
			((AbstractText) textShape.getGraphicsAlgorithm()).setValue(ModelUtil.getName(element));
			layoutPictogramElement(context.getPictogramElement());
		}
		return true;
	}
}