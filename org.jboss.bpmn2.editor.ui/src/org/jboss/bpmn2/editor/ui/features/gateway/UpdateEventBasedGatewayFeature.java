package org.jboss.bpmn2.editor.ui.features.gateway;

import org.eclipse.bpmn2.EventBasedGateway;
import org.eclipse.bpmn2.EventBasedGatewayType;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.utils.ShapeUtil;
import org.jboss.bpmn2.editor.utils.StyleUtil;

public class UpdateEventBasedGatewayFeature extends AbstractUpdateFeature {

	public UpdateEventBasedGatewayFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		Object o = getBusinessObjectForPictogramElement(context.getPictogramElement());
		return o != null && o instanceof EventBasedGateway;
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		IPeService service = Graphiti.getPeService();

		boolean instantiate = Boolean.parseBoolean(service.getPropertyValue(context.getPictogramElement(),
		        EventBasedGatewayFeatureContainer.INSTANTIATE_PROPERTY));
		EventBasedGatewayType gatewayType = EventBasedGatewayType.getByName(service.getPropertyValue(
		        context.getPictogramElement(), EventBasedGatewayFeatureContainer.EVENT_GATEWAY_TYPE_PROPERTY));

		EventBasedGateway gateway = (EventBasedGateway) getBusinessObjectForPictogramElement(context
		        .getPictogramElement());

		boolean changed = instantiate != gateway.isInstantiate() || gatewayType != gateway.getEventGatewayType();
		return changed ? Reason.createTrueReason() : Reason.createFalseReason();
	}

	@Override
	public boolean update(IUpdateContext context) {
		IPeService service = Graphiti.getPeService();

		EventBasedGateway gateway = (EventBasedGateway) getBusinessObjectForPictogramElement(context
		        .getPictogramElement());

		clearGateway(context.getPictogramElement());

		if (gateway.isInstantiate()) {
			if (gateway.getEventGatewayType() == EventBasedGatewayType.PARALLEL) {
				drawParallelMultipleEventBased((ContainerShape) context.getPictogramElement());
			} else {
				drawExclusiveEventBased((ContainerShape) context.getPictogramElement());
			}
		} else {
			drawEventBased((ContainerShape) context.getPictogramElement());
		}

		service.setPropertyValue(context.getPictogramElement(), EventBasedGatewayFeatureContainer.INSTANTIATE_PROPERTY,
		        Boolean.toString(gateway.isInstantiate()));
		service.setPropertyValue(context.getPictogramElement(),
		        EventBasedGatewayFeatureContainer.EVENT_GATEWAY_TYPE_PROPERTY, gateway.getEventGatewayType().getName());
		return true;
	}

	private void clearGateway(PictogramElement element) {
		ShapeUtil.clearGateway(element);
	}

	private void drawEventBased(ContainerShape container) {
		Ellipse outer = ShapeUtil.createGatewayOuterCircle(container);
		outer.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		Ellipse inner = ShapeUtil.createGatewayInnerCircle(container);
		inner.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		Polygon pentagon = ShapeUtil.createGatewayPentagon(container);
		pentagon.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		pentagon.setFilled(false);
	}

	private void drawExclusiveEventBased(ContainerShape container) {
		Ellipse ellipse = ShapeUtil.createGatewayOuterCircle(container);
		ellipse.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		Polygon pentagon = ShapeUtil.createGatewayPentagon(container);
		pentagon.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		pentagon.setFilled(false);
	}

	private void drawParallelMultipleEventBased(ContainerShape container) {
		Ellipse ellipse = ShapeUtil.createGatewayOuterCircle(container);
		ellipse.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		Polygon cross = ShapeUtil.createEventGatewayParallelCross(container);
		cross.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
	}
}