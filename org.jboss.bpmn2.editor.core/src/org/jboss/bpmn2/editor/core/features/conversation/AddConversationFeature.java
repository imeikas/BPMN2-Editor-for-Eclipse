package org.jboss.bpmn2.editor.core.features.conversation;

import org.eclipse.bpmn2.Conversation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.styles.AdaptedGradientColoredAreas;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.PredefinedColoredAreas;
import org.jboss.bpmn2.editor.core.features.AbstractBpmnAddFeature;
import org.jboss.bpmn2.editor.utils.AnchorUtil;
import org.jboss.bpmn2.editor.utils.StyleUtil;

public class AddConversationFeature extends AbstractBpmnAddFeature {

	public AddConversationFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context.getTargetContainer().equals(getDiagram());
	}

	@Override
	public PictogramElement add(IAddContext context) {
		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();
		Conversation c = (Conversation) context.getNewObject();

		int w = context.getWidth() > 0 ? context.getWidth() : 30;
		int h = context.getHeight() > 0 ? context.getHeight() : 30;

		ContainerShape containerShape = peService.createContainerShape(context.getTargetContainer(), true);

		int w_5th = w / 5;

		int[] xy = { w_5th, 0, w_5th * 4, 0, w, h / 2, w_5th * 4, h, w_5th, h, 0, h / 2 };
		Polygon hexagon = gaService.createPolygon(containerShape, xy);
		
		StyleUtil.applyBGStyle(hexagon, this);
		
		peService.createChopboxAnchor(containerShape);
		AnchorUtil.addFixedPointAnchors(containerShape, hexagon);
		
		if (c.eResource() == null) {
			getDiagram().eResource().getContents().add(c);
		}
		
		link(containerShape, c);
		createDIShape(containerShape, c);
		return containerShape;
	}
}