package org.jboss.bpmn2.editor.core.features.activity;

import static org.jboss.bpmn2.editor.core.features.activity.ActivityCompensateMarkerUpdateFeature.IS_COMPENSATE_PROPERTY;
import static org.jboss.bpmn2.editor.core.features.activity.ActivityLoopAndMultiInstanceMarkerUpdateFeature.IS_LOOP_OR_MULTI_INSTANCE;
import static org.jboss.bpmn2.editor.core.features.activity.ActivityLoopAndMultiInstanceMarkerUpdateFeature.getLoopCharacteristicsValue;

import org.eclipse.bpmn2.Activity;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.features.AbstractBpmnAddFeature;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;
import org.jboss.bpmn2.editor.utils.AnchorUtil;
import org.jboss.bpmn2.editor.utils.ShapeUtil;
import org.jboss.bpmn2.editor.utils.StyleUtil;

public abstract class AbstractAddActivityFeature extends AbstractBpmnAddFeature {

	public AbstractAddActivityFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context) && FeatureSupport.isTargetLaneOnTop(context);
		boolean intoParticipant = FeatureSupport.isTargetParticipant(context);
		return intoDiagram || intoLane || intoParticipant;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		Activity activity = (Activity) context.getNewObject();

		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();

		int paddingBottom = ShapeUtil.ACTIVITY_BOTTOM_PADDING;
		int width = context.getWidth() > 0 ? context.getWidth() : getWidth();
		int height = context.getHeight() > 0 ? context.getHeight() + paddingBottom : getHeight() + paddingBottom;

		ContainerShape containerShape = peService.createContainerShape(context.getTargetContainer(), true);
		Rectangle invisibleRect = gaService.createInvisibleRectangle(containerShape);
		gaService.setLocationAndSize(invisibleRect, context.getX(), context.getY(), width, height);

		Shape rectShape = peService.createShape(containerShape, false);
		RoundedRectangle rect = gaService.createRoundedRectangle(rectShape, 5, 5);

		StyleUtil.applyBGStyle(rect, this);

		gaService.setLocationAndSize(rect, 0, 0, width, height - paddingBottom);
		link(rectShape, activity);
		decorateActivityRectangle(rect);

		ContainerShape markerContainer = peService.createContainerShape(containerShape, false);
		Rectangle markerInvisibleRect = gaService.createInvisibleRectangle(markerContainer);
		int h = 10;
		int y = height - paddingBottom - h - 3 - getMarkerContainerOffset();
		gaService.setLocationAndSize(markerInvisibleRect, 0, y, invisibleRect.getWidth(), h);
		peService.setPropertyValue(markerContainer, ShapeUtil.ACTIVITY_MARKER_CONTAINER, Boolean.toString(true));

		hook(activity, containerShape, context, width, height); // hook for subclasses to inject extra code

		peService.createChopboxAnchor(containerShape);
		AnchorUtil.addFixedPointAnchors(containerShape, rect);

		createDIShape(containerShape, activity);

		Graphiti.getPeService().setPropertyValue(containerShape, IS_COMPENSATE_PROPERTY, Boolean.toString(false));
		Graphiti.getPeService().setPropertyValue(containerShape, IS_LOOP_OR_MULTI_INSTANCE,
				getLoopCharacteristicsValue(activity).getName());
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