package org.jboss.bpmn2.editor.core.features.artifact;

import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class AddTextAnnotationFeature extends AbstractAddShapeFeature {
	
	private FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};
	
	public AddTextAnnotationFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    public boolean canAdd(IAddContext context) {
		boolean isAnnotation = context.getNewObject() instanceof TextAnnotation;
		boolean intoDiagram = context.getTargetContainer() instanceof Diagram;
		boolean intoLane = support.isTargetLane(context) && support.isTargetLaneOnTop(context);
		return isAnnotation && (intoDiagram || intoLane);
	}

	@Override
    public PictogramElement add(IAddContext context) {
		TextAnnotation annotation = (TextAnnotation) context.getNewObject();

		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(context.getTargetContainer(), true);

		IGaService gaService = Graphiti.getGaService();
		
		int height = context.getWidth() > 0 ? context.getWidth() : 50;
		int width = context.getHeight() > 0 ? context.getHeight() : 100;
		int commentEdge = 15;
		
		Rectangle rect = gaService.createInvisibleRectangle(containerShape);
		gaService.setLocationAndSize(rect, context.getX(), context.getY(), width, height);
		
		Shape lineShape = peCreateService.createShape(containerShape, false);
		Polyline line = gaService.createPolyline(lineShape, new int[] {commentEdge, 0, 0, 0, 0, height, commentEdge, height});
		line.setStyle(StyleUtil.getStyleForClass(getDiagram()));
		line.setLineWidth(2);
		gaService.setLocationAndSize(line, 0, 0, commentEdge, height);
		
		Shape textShape = peCreateService.createShape(containerShape, false);
		MultiText text = gaService.createDefaultMultiText(textShape, annotation.getText());
		text.setStyle(StyleUtil.getStyleForText(getDiagram()));
		text.setVerticalAlignment(Orientation.ALIGNMENT_TOP);
		gaService.setLocationAndSize(text, 5, 5, width - 5, height - 5);
		
		link(containerShape, annotation);
		link(textShape, annotation);

		peCreateService.createChopboxAnchor(containerShape);
		layoutPictogramElement(containerShape);
		return containerShape;
    }
}