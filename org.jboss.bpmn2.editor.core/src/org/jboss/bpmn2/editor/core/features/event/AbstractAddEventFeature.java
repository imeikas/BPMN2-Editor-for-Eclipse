package org.jboss.bpmn2.editor.core.features.event;

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
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public abstract class AbstractAddEventFeature extends AbstractAddShapeFeature {

	public AbstractAddEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	private FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};

	@Override
	public boolean canAdd(IAddContext context) {
		boolean assignable = getEventClass().isAssignableFrom(context.getNewObject().getClass());
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = support.isTargetLane(context) && support.isTargetLaneOnTop(context);
		boolean intoParticipant = support.isTargetParticipant(context);
		return assignable && (intoDiagram || intoLane || intoParticipant);
	}

	@Override
	public PictogramElement add(IAddContext context) {
		Event e = (Event) context.getNewObject();

		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(context.getTargetContainer(), true);

		IGaService gaService = Graphiti.getGaService();

		Rectangle invisibleRect = gaService.createInvisibleRectangle(containerShape);
		gaService.setLocationAndSize(invisibleRect, context.getX(), context.getY(), ShapeUtil.EVENT_SIZE,
		        ShapeUtil.EVENT_SIZE + ShapeUtil.EVENT_TEXT_AREA);

		Shape ellipseShape = peCreateService.createShape(containerShape, false);
		Ellipse ellipse = ShapeUtil.createEventShape(ellipseShape);
		ellipse.setStyle(StyleUtil.getStyleForClass(getDiagram()));
		decorateEllipse(ellipse);
		AdaptedGradientColoredAreas gradient = PredefinedColoredAreas.getBlueWhiteAdaptions();
		gaService.setRenderingStyle(ellipse, gradient);
		gaService.setLocation(ellipse, 0, 0);

		Shape textShape = peCreateService.createShape(containerShape, false);
		Text text = gaService.createDefaultText(textShape, e.getName());
		text.setStyle(StyleUtil.getStyleForText(getDiagram()));
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_BOTTOM);
		text.setBackground(manageColor(IColorConstant.RED));
		decorateText(text);
		gaService.setLocationAndSize(text, 0, ShapeUtil.EVENT_SIZE, ShapeUtil.EVENT_SIZE, ShapeUtil.EVENT_TEXT_AREA);

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

	protected void decorateEllipse(Ellipse e) {
	}

	protected void decorateText(Text t) {
	}

	protected abstract Class<? extends Event> getEventClass();
}
