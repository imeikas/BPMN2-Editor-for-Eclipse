package org.jboss.bpmn2.editor.core.features.lane;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.AdaptedGradientColoredAreas;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.PredefinedColoredAreas;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class AddLaneFeature extends AbstractAddShapeFeature {

	private FeatureSupport support = new FeatureSupport() {
		@Override
		protected Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};

	public AddLaneFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean isLane = context.getNewObject() instanceof Lane;
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = support.isTargetLane(context);
		return isLane && (intoDiagram || intoLane);
	}

	@Override
	public PictogramElement add(IAddContext context) {
		Lane lane = (Lane) context.getNewObject();

		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(context.getTargetContainer(), true);
		IGaService gaService = Graphiti.getGaService();

		int width = 600;
		int height = 100;

		Rectangle rect = gaService.createRectangle(containerShape);
		rect.setStyle(StyleUtil.getStyleForClass(getDiagram()));
		AdaptedGradientColoredAreas gradient = PredefinedColoredAreas.getBlueWhiteAdaptions();
		gaService.setRenderingStyle(rect, gradient);

		if(support.isTargetLane(context)) {
			ContainerShape targetContainer = context.getTargetContainer();
			Lane targetLane = (Lane) getBusinessObjectForPictogramElement(targetContainer);
			GraphicsAlgorithm targetGa = targetContainer.getGraphicsAlgorithm();
			List<Shape> shapes = getFlowNodeShapes(context, lane);
			int w = targetGa.getWidth();
			int h = targetGa.getHeight();
			int numberOfLanes = targetLane.getChildLaneSet().getLanes().size();
			
			if (numberOfLanes == 1) {
				gaService.setLocationAndSize(rect, 15, 0, w - 15, h);
				for (Shape s : shapes) {
					Graphiti.getPeService().sendToFront(s);
				}
			} else {
				resizeRecursively(targetContainer, height);
				gaService.setLocationAndSize(rect, 15, h - 1, w - 15, height + 1);
			}
			
		} else {
			gaService.setLocationAndSize(rect, context.getX(), context.getY(), width, height);
		}

		Shape textShape = peCreateService.createShape(containerShape, false);
		Text text = gaService.createText(textShape, lane.getName());
		text.setStyle(StyleUtil.getStyleForText(getDiagram()));
		text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setAngle(-90);
		gaService.setLocationAndSize(text, 0, 0, 15, height);

		link(containerShape, lane);
		link(textShape, lane);

		peCreateService.createChopboxAnchor(containerShape);
		layoutPictogramElement(containerShape);
		
		return containerShape;
	}
	
	private List<Shape> getFlowNodeShapes(IAddContext context, Lane lane) {
		List<FlowNode> nodes = lane.getFlowNodeRefs();
		List<Shape> shapes = new ArrayList<Shape>();
		for (Shape s : context.getTargetContainer().getChildren()) {
			Object bo = getBusinessObjectForPictogramElement(s);
			if (bo != null && nodes.contains(bo)) {
				shapes.add(s);
			}
		}
		return shapes;
	}
	
	private void resizeRecursively(ContainerShape container, int height) {
		if(container == null) {
			return;
		}
		
		Object bo = getBusinessObjectForPictogramElement(container);
		if(bo == null || !(bo instanceof Lane)) {
			return;
		}
		
		IGaService gaService = Graphiti.getGaService();
		GraphicsAlgorithm ga =  container.getGraphicsAlgorithm();
		
		gaService.setSize(ga, ga.getWidth(), ga.getHeight() + height);
		
		resizeRecursively(container.getContainer(), height);
	}
}