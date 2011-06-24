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
package org.eclipse.bpmn2.modeler.core.features.data;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmnAddFeature;
import org.eclipse.bpmn2.modeler.core.features.UpdateBaseElementNameFeature;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.GraphicsUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public abstract class AddDataFeature<T extends BaseElement> extends AbstractBpmnAddFeature {

	public AddDataFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context) && FeatureSupport.isTargetLaneOnTop(context);
		boolean intoParticipant = FeatureSupport.isTargetParticipant(context);
		boolean intoSubProcess = FeatureSupport.isTargetSubProcess(context);
		return intoDiagram || intoLane || intoParticipant || intoSubProcess;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();
		@SuppressWarnings("unchecked")
		T t = (T) context.getNewObject();

		int width = GraphicsUtil.DATA_WIDTH;
		int height = GraphicsUtil.DATA_HEIGHT;
		int e = 10;
		int textArea = 15;
		
		ContainerShape container = peService.createContainerShape(context.getTargetContainer(), true);
		Rectangle invisibleRect = gaService.createInvisibleRectangle(container);
		gaService.setLocationAndSize(invisibleRect, context.getX(), context.getY(), width, height + textArea);

		Shape rectShape = peService.createShape(container, false);
		Polygon rect = gaService.createPolygon(rectShape, new int[] { 0, 0, width - e, 0, width, e, width, height, 0,
				height });
		rect.setLineWidth(1);
		StyleUtil.applyBGStyle(rect, this);
		decorate(rect);

		int p = width - e - 1;
		Polyline edge = gaService.createPolyline(rect, new int[] { p, 0, p, e + 1, width, e + 1 });
		edge.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		edge.setLineWidth(1);

		if (isSupportCollectionMarkers()) {
			int whalf = width / 2;
			createCollectionShape(container, new int[] { whalf - 2, height - 8, whalf - 2, height });
			createCollectionShape(container, new int[] { whalf, height - 8, whalf, height });
			createCollectionShape(container, new int[] { whalf + 2, height - 8, whalf + 2, height });

			Graphiti.getPeService().setPropertyValue(container, Properties.COLLECTION_PROPERTY, Boolean.toString(false));
		}
		
		Shape textShape = peService.createShape(container, false);
		peService.setPropertyValue(textShape, UpdateBaseElementNameFeature.TEXT_ELEMENT, Boolean.toString(true));
		Text text = gaService.createDefaultText(getDiagram(), textShape, getName(t));
		text.setStyle(StyleUtil.getStyleForText(getDiagram()));
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_TOP);
		gaService.setLocationAndSize(text, 0, height, width, textArea);
		
		peService.createChopboxAnchor(container);
		AnchorUtil.addFixedPointAnchors(container, invisibleRect);
		createDIShape(container, t);
		layoutPictogramElement(container);
		return container;
	}

	private Shape createCollectionShape(ContainerShape container, int[] xy) {
		IPeService peService = Graphiti.getPeService();
		IGaService gaService = Graphiti.getGaService();
		Shape collectionShape = peService.createShape(container, false);
		Polyline line = gaService.createPolyline(collectionShape, xy);
		line.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		line.setLineWidth(1);
		line.setLineVisible(false);
		peService.setPropertyValue(collectionShape, Properties.HIDEABLE_PROPERTY, Boolean.toString(true));
		return collectionShape;
	}

	protected void decorate(Polygon p) {
	}

	protected boolean isSupportCollectionMarkers() {
		return true;
	}
	
	public abstract String getName(T t);
}