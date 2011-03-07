package org.jboss.bpmn2.editor.core.features.gateway;

import static org.jboss.bpmn2.editor.core.utils.GraphicsUtil.EVENT_SIZE;
import static org.jboss.bpmn2.editor.core.utils.GraphicsUtil.EVENT_TEXT_AREA;

import org.eclipse.bpmn2.Gateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.features.AbstractBpmnAddFeature;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil;
import org.jboss.bpmn2.editor.core.utils.FeatureSupport;
import org.jboss.bpmn2.editor.core.utils.GraphicsUtil;
import org.jboss.bpmn2.editor.core.utils.StyleUtil;

public class DefaultAddGatewayFeature extends AbstractBpmnAddFeature {

	public DefaultAddGatewayFeature(IFeatureProvider fp) {
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
		Gateway addedGateway = (Gateway) context.getNewObject();
		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();
		
		int d = 2 * GraphicsUtil.GATEWAY_RADIUS;
		int p = GraphicsUtil.GATEWAY_TEXT_AREA;
		
		ContainerShape containerShape = peService.createContainerShape(context.getTargetContainer(), true);
		Rectangle rect = gaService.createInvisibleRectangle(containerShape);
		gaService.setLocationAndSize(rect, context.getX(), context.getY(), d, d + p);
		
		Shape gatewayShape = peService.createShape(containerShape, false);
		Polygon gateway = GraphicsUtil.createGateway(gatewayShape);
		StyleUtil.applyBGStyle(gateway, this);
		gaService.setLocationAndSize(gateway, 0, 0, d, d);
		decorateGateway(containerShape);
		
		Shape textShape = peService.createShape(containerShape, false);
		peService.setPropertyValue(textShape, EVENT_ELEMENT, EVENT_TEXT);
		Text text = gaService.createDefaultText(textShape, e.getName());
		text.setStyle(StyleUtil.getStyleForText(getDiagram()));
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_TOP);
		gaService.setLocationAndSize(text, 0, EVENT_SIZE, EVENT_SIZE, EVENT_TEXT_AREA);
		
		createDIShape(containerShape, addedGateway);

		
		
		peService.createChopboxAnchor(containerShape);
		AnchorUtil.addFixedPointAnchors(containerShape, gateway);
		
		return containerShape;
	}

	protected void decorateGateway(ContainerShape container) {
	}
}