package org.jboss.bpmn2.editor.core.features.event;

import static org.jboss.bpmn2.editor.core.features.event.SizeConstants.HEIGHT;
import static org.jboss.bpmn2.editor.core.features.event.SizeConstants.TEXT_AREA_HEIGHT;
import static org.jboss.bpmn2.editor.core.features.event.SizeConstants.WIDTH;

import org.eclipse.bpmn2.BaseElement;
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
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;
import org.eclipse.graphiti.util.PredefinedColoredAreas;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public abstract class AbstractAddEventFeature extends AbstractAddShapeFeature {

	public AbstractAddEventFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	private FeatureSupport support = new FeatureSupport() {
		@Override
		protected Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};
	
	@Override
	public boolean canAdd(IAddContext context) {
		boolean assignable = getBPMNClass().isAssignableFrom(context.getNewObject().getClass());
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = support.isTargetLane(context) && support.isTargetLaneOnTop(context);
	    return assignable && (intoDiagram || intoLane);
	}
	
	@Override
	public PictogramElement add(IAddContext context) {
		Event e = (Event) context.getNewObject();

		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(context.getTargetContainer(), true);

		IGaService gaService = Graphiti.getGaService();

		Rectangle invisibleRect = gaService.createInvisibleRectangle(containerShape);
		gaService.setLocationAndSize(invisibleRect, context.getX(), context.getY(), WIDTH, HEIGHT + TEXT_AREA_HEIGHT);
		
		Shape ellipseShape = peCreateService.createShape(containerShape, false);
		Ellipse ellipse = gaService.createEllipse(ellipseShape);
		ellipse.setStyle(StyleUtil.getStyleForClass(getDiagram()));
		enhanceEllipse(ellipse);
		AdaptedGradientColoredAreas gradient = PredefinedColoredAreas.getBlueWhiteAdaptions();
		gaService.setRenderingStyle(ellipse, gradient);
		gaService.setLocationAndSize(ellipse, 0, 0, WIDTH, HEIGHT);

		Shape textShape = peCreateService.createShape(containerShape, false);
		Text text = gaService.createDefaultText(textShape, e.getName());
		text.setStyle(StyleUtil.getStyleForText(getDiagram()));
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_BOTTOM);
		text.setBackground(manageColor(IColorConstant.RED));
		enhanceText(text);
		gaService.setLocationAndSize(text, 0, HEIGHT, WIDTH, TEXT_AREA_HEIGHT);

		if (e.eResource() == null) {
			getDiagram().eResource().getContents().add(e);
		}
		
		link(ellipseShape, e);
		link(textShape, e);
		link(containerShape, e);

		peCreateService.createChopboxAnchor(containerShape);
		layoutPictogramElement(containerShape);
		return containerShape;
	}

	protected void enhanceEllipse(Ellipse e) {
	}

	protected void enhanceText(Text t) {
	}
	
    protected abstract Class<? extends BaseElement> getBPMNClass();
}
