package org.jboss.bpmn2.editor.core.features.gateway;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Gateway;
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
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.StyleUtil;
import org.jboss.bpmn2.editor.core.features.task.AbstractBpmnAddFeature;

public class DefaultAddGatewayFeature extends AbstractBpmnAddFeature {

	public static final int RADIUS = 25;

	protected FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return BusinessObjectUtil.getFirstElementOfType(element, BaseElement.class);
		}
	};

	public DefaultAddGatewayFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = support.isTargetLane(context) && support.isTargetLaneOnTop(context);
		boolean intoParticipant = support.isTargetParticipant(context);
		boolean intoSubProcess = support.isTargetSubProcess(context);
		return intoDiagram || intoLane || intoParticipant || intoSubProcess;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		Gateway addedGateway = (Gateway) context.getNewObject();
		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();

		ContainerShape containerShape = peService.createContainerShape(context.getTargetContainer(), true);

		Polygon gateway = ShapeUtil.createGateway(containerShape);
		gateway.setStyle(StyleUtil.getStyleForClass(getDiagram()));

		AdaptedGradientColoredAreas gradient = PredefinedColoredAreas.getBlueWhiteAdaptions();
		gaService.setRenderingStyle(gateway, gradient);

		gaService.setLocationAndSize(gateway, context.getX(), context.getY(), 2 * RADIUS, 2 * RADIUS);

		decorateGateway(containerShape);

		if (addedGateway.eResource() == null) {
			getDiagram().eResource().getContents().add(addedGateway);
		}

		createDIShape(containerShape, addedGateway, RADIUS * 2, RADIUS * 2, context.getX(), context.getY());

		peService.createChopboxAnchor(containerShape);
		return containerShape;
	}

	protected void decorateGateway(ContainerShape container) {
	}
}