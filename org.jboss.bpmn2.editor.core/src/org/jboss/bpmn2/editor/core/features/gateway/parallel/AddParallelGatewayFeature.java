package org.jboss.bpmn2.editor.core.features.gateway.parallel;

import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.AdaptedGradientColoredAreas;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.PredefinedColoredAreas;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class AddParallelGatewayFeature extends AbstractAddShapeFeature {
	
	private static final int RADIUS = 25;
	
	private FeatureSupport support = new FeatureSupport() {
		@Override
		protected Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};
	
	public AddParallelGatewayFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    public boolean canAdd(IAddContext context) {
		boolean isParallelGateway = context.getNewObject() instanceof ParallelGateway;
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = support.isTargetLane(context) && support.isTargetLaneOnTop(context);
		return isParallelGateway && (intoDiagram || intoLane);
	}

	@Override
    public PictogramElement add(IAddContext context) {
		ParallelGateway addedGateway = (ParallelGateway) context.getNewObject();

		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(context.getTargetContainer(), true);

		IGaService gaService = Graphiti.getGaService();
		int[] xy = new int[] { 0, RADIUS, RADIUS, 0, 2 * RADIUS, RADIUS, RADIUS, 2 * RADIUS };
		Polygon diamond = gaService.createPolygon(containerShape, xy);
		diamond.setStyle(StyleUtil.getStyleForClass(getDiagram()));

		AdaptedGradientColoredAreas gradient = PredefinedColoredAreas.getBlueWhiteAdaptions();
		gaService.setRenderingStyle(diamond, gradient);

		gaService.setLocationAndSize(diamond, context.getX(), context.getY(), 2 * RADIUS, 2 * RADIUS);
		
		Polyline verticalLine = gaService.createPolyline(diamond, new int [] {24, 7, 24, 43});
		verticalLine.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		verticalLine.setLineWidth(3);
		
		Polyline horizontalLine = gaService.createPolyline(diamond, new int [] {7, 24, 43, 24});
		horizontalLine.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		horizontalLine.setLineWidth(3);
		
		if (addedGateway.eResource() == null) {
			getDiagram().eResource().getContents().add(addedGateway);
		}

		link(containerShape, addedGateway);

		peCreateService.createChopboxAnchor(containerShape);
		return containerShape;
	}
}
