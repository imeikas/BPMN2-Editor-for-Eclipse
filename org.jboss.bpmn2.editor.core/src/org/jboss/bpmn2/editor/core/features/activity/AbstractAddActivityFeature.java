package org.jboss.bpmn2.editor.core.features.activity;

import org.eclipse.bpmn2.Activity;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.AdaptedGradientColoredAreas;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.PredefinedColoredAreas;
import org.jboss.bpmn2.editor.core.features.AbstractBpmnAddFeature;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public abstract class AbstractAddActivityFeature extends AbstractBpmnAddFeature {

	protected FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};

	public AbstractAddActivityFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = support.isTargetLane(context) && support.isTargetLaneOnTop(context);
		boolean intoParticipant = support.isTargetParticipant(context);
		return intoDiagram || intoLane || intoParticipant;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		Activity activity = (Activity) context.getNewObject();

		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();

		int paddingBottom = ShapeUtil.ACTIVITY_BOTTOM_PADDING;
		int width = context.getWidth() > 0 ? context.getWidth() : getWidth();
		int height = context.getHeight() > 0 ? context.getHeight() : getHeight() + paddingBottom;

		ContainerShape containerShape = peService.createContainerShape(context.getTargetContainer(), true);
		Rectangle invisibleRect = gaService.createInvisibleRectangle(containerShape);
		gaService.setLocationAndSize(invisibleRect, context.getX(), context.getY(), width, height);

		Shape rectShape = peService.createShape(containerShape, false);
		RoundedRectangle rect = gaService.createRoundedRectangle(rectShape, 5, 5);
		rect.setStyle(StyleUtil.getStyleForClass(getDiagram()));
		AdaptedGradientColoredAreas gradient = PredefinedColoredAreas.getBlueWhiteAdaptions();
		gaService.setRenderingStyle(rect, gradient);
		gaService.setLocationAndSize(rect, 0, 0, width, height - paddingBottom);
		link(rectShape, activity);
		decorateActivityRectangle(rect);

		hook(activity, containerShape, context, width, height); // hook for subclasses to inject extra code

		ContainerShape markerContainer = peService.createContainerShape(containerShape, false);
		Rectangle markerInvisibleRect = gaService.createInvisibleRectangle(markerContainer);
		int h = 10;
		int y = height - paddingBottom - h - getMarkerContainerOffset();
		gaService.setLocationAndSize(markerInvisibleRect, 0, y, invisibleRect.getWidth(), h);
		peService.setPropertyValue(markerContainer, ShapeUtil.ACTIVITY_MARKER_CONTAINER, Boolean.toString(true));

		ChopboxAnchor anchor = peService.createChopboxAnchor(containerShape);
		anchor.setReferencedGraphicsAlgorithm(rect);

		createDIShape(containerShape, activity);

		if (activity.eResource() == null) {
			getDiagram().eResource().getContents().add(activity);
		}

		Graphiti.getPeService().setPropertyValue(containerShape,
				ActivityCompensateMarkerUpdateFeature.IS_COMPENSATE_PROPERTY, Boolean.toString(false));

		layoutPictogramElement(containerShape);

		return containerShape;
	}

	protected void decorateActivityRectangle(RoundedRectangle rect) {
	}

	protected void hook(Activity activity, ContainerShape container, IAddContext context, int width, int height) {
	}

	protected int getMarkerContainerOffset() {
		return 0;
	}

	protected abstract int getWidth();

	protected abstract int getHeight();
}