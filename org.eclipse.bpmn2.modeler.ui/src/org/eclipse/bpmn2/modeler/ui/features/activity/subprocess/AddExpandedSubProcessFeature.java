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

import static org.eclipse.bpmn2.modeler.ui.features.activity.subprocess.SubProcessFeatureContainer.TRIGGERED_BY_EVENT;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.modeler.core.features.activity.AbstractAddActivityFeature;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class AddExpandedSubProcessFeature extends AbstractAddActivityFeature {

	public AddExpandedSubProcessFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void hook(Activity activity, ContainerShape container, IAddContext context, int width, int height) {
		super.hook(activity, container, context, width, height);
		Graphiti.getPeService().setPropertyValue(container, TRIGGERED_BY_EVENT, "false");

		IPeService peService = Graphiti.getPeService();
		IGaService gaService = Graphiti.getGaService();

		Shape textShape = peService.createShape(container, false);
		Text text = gaService.createDefaultText(getDiagram(), textShape, activity.getName());
		gaService.setLocationAndSize(text, 5, 5, width - 10, 15);
		text.setStyle(StyleUtil.getStyleForText(getDiagram()));
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
		text.getFont().setBold(true);
		link(textShape, activity);
	}

	@Override
	protected int getWidth() {
		return GraphicsUtil.SUB_PROCEESS_DEFAULT_WIDTH;
	}

	@Override
	protected int getHeight() {
		return GraphicsUtil.SUB_PROCESS_DEFAULT_HEIGHT;
	}
}