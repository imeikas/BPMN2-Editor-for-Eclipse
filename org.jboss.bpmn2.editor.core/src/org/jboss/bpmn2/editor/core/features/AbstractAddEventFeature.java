package org.jboss.bpmn2.editor.core.features;

import org.eclipse.bpmn2.Event;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.AdaptedGradientColoredAreas;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.PredefinedColoredAreas;

public abstract class AbstractAddEventFeature extends AbstractAddShapeFeature {
	
	public AbstractAddEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public PictogramElement add(IAddContext context) {
		Event e = (Event) context.getNewObject();
		Diagram targetDiagram = (Diagram) context.getTargetContainer();
		
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(targetDiagram, true);
		
		IGaService gaService = Graphiti.getGaService();
		
		int width = 35;
		int height = 35;
		int extraSpaceBottom = 15;
		
		Rectangle invisibleRect = gaService.createInvisibleRectangle(containerShape);
		gaService.setLocationAndSize(invisibleRect, context.getX(), context.getY(), width, height + extraSpaceBottom);
		
		Ellipse ellipse = gaService.createEllipse(invisibleRect);
		ellipse.setStyle(StyleUtil.getStyleForClass(getDiagram()));
		enhanceEllipse(ellipse);
		AdaptedGradientColoredAreas gradient = PredefinedColoredAreas.getBlueWhiteAdaptions();
		gaService.setRenderingStyle(ellipse, gradient);
		gaService.setLocationAndSize(ellipse, 0, 0, width, height);
		
		Shape shape = peCreateService.createShape(containerShape, false);
		Text text = gaService.createDefaultText(shape, e.getName());
		text.setStyle(StyleUtil.getStyleForText(getDiagram()));
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_BOTTOM);
		enhanceText(text);
		gaService.setLocationAndSize(text, 0, height, width, extraSpaceBottom);
		
		if (e.eResource() == null) {
			getDiagram().eResource().getContents().add(e);
		}
		
		link(shape, e);
		link(containerShape, e);
		
		peCreateService.createChopboxAnchor(containerShape);
		return containerShape;
	}
	
	protected void enhanceEllipse(Ellipse e) {}
	
	protected void enhanceText(Text t) {}
}
