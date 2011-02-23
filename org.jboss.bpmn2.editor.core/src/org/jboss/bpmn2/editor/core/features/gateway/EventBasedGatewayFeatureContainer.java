package org.jboss.bpmn2.editor.core.features.gateway;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.EventBasedGateway;
import org.eclipse.bpmn2.EventBasedGatewayType;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class EventBasedGatewayFeatureContainer extends AbstractGatewayFeatureContainer {

	static final String INSTANTIATE_PROPERTY = "instantiate";
	static final String EVENT_GATEWAY_TYPE_PROPERTY = "eventGatewayType";

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof EventBasedGateway;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateEventBasedGatewayFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new DefaultAddGatewayFeature(fp) {
			@Override
			protected void decorateGateway(ContainerShape container) {
				Ellipse outer = ShapeUtil.createGatewayOuterCircle(container);
				outer.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				Ellipse inner = ShapeUtil.createGatewayInnerCircle(container);
				inner.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				Polygon pentagon = ShapeUtil.createGatewayPentagon(container);
				pentagon.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				pentagon.setFilled(false);
			}

			@Override
			public PictogramElement add(IAddContext context) {
				PictogramElement element = super.add(context);
				IPeService service = Graphiti.getPeService();
				service.setPropertyValue(element, INSTANTIATE_PROPERTY, "false");
				service.setPropertyValue(element, EVENT_GATEWAY_TYPE_PROPERTY,
				        EventBasedGatewayType.EXCLUSIVE.getName());
				return element;
			}
		};
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		return new UpdateEventBasedGatewayFeature(fp);
	}

	public static class CreateEventBasedGatewayFeature extends AbstractCreateGatewayFeature {

		public CreateEventBasedGatewayFeature(IFeatureProvider fp) {
			super(fp, "Event-Based Gateway", "Represents a branching point in the process");
		}

		@Override
		protected Gateway createFlowElement(ICreateContext context) {
			EventBasedGateway gateway = ModelHandler.FACTORY.createEventBasedGateway();
			gateway.setInstantiate(false);
			gateway.setEventGatewayType(EventBasedGatewayType.EXCLUSIVE);
			return gateway;
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_EVENT_BASED_GATEWAY;
		}
	}
}