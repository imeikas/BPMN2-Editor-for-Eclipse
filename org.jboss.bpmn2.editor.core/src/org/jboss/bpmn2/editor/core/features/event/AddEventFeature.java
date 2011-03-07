package org.jboss.bpmn2.editor.core.features.event;

import static org.jboss.bpmn2.editor.core.utils.GraphicsUtil.EVENT_SIZE;
import static org.jboss.bpmn2.editor.core.utils.GraphicsUtil.EVENT_TEXT_AREA;
import static org.jboss.bpmn2.editor.core.utils.GraphicsUtil.createEventShape;

import org.eclipse.bpmn2.Event;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.features.AbstractBpmnAddFeature;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil;
import org.jboss.bpmn2.editor.core.utils.FeatureSupport;
import org.jboss.bpmn2.editor.core.utils.StyleUtil;

public class AddEventFeature extends AbstractBpmnAddFeature {
	
	public static final String EVENT_ELEMENT = "event.graphics.element";
	public static final String EVENT_TEXT = "event.graphics.element.text";
	public static final String EVENT_CIRCLE = "event.graphics.element.circle";
	
	public AddEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context) && FeatureSupport.isTargetLaneOnTop(context);
		boolean intoParticipant = FeatureSupport.isTargetParticipant(context);
		boolean intoSubProcess = FeatureSupport.isTargetSubProcess(context);
		return intoDiagram || intoLane || intoParticipant || intoSubProcess;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		Event e = (Event) context.getNewObject();

		IPeService peService = Graphiti.getPeService();
		ContainerShape containerShape = peService.createContainerShape(context.getTargetContainer(), true);

		IGaService gaService = Graphiti.getGaService();

		Rectangle invisibleRect = gaService.createInvisibleRectangle(containerShape);
		gaService.setLocationAndSize(invisibleRect, context.getX(), context.getY(), EVENT_SIZE, EVENT_SIZE
		        + EVENT_TEXT_AREA);

		Shape ellipseShape = peService.createShape(containerShape, false);
		peService.setPropertyValue(ellipseShape, EVENT_ELEMENT, EVENT_CIRCLE);
		Ellipse ellipse = createEventShape(ellipseShape);
		StyleUtil.applyBGStyle(ellipse, this);
		decorateEllipse(ellipse);

		Shape textShape = peService.createShape(containerShape, false);
		peService.setPropertyValue(textShape, EVENT_ELEMENT, EVENT_TEXT);
		Text text = gaService.createDefaultText(textShape, e.getName());
		text.setStyle(StyleUtil.getStyleForText(getDiagram()));
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_TOP);
		gaService.setLocationAndSize(text, 0, EVENT_SIZE, EVENT_SIZE, EVENT_TEXT_AREA);


		peService.createChopboxAnchor(containerShape);
		AnchorUtil.addFixedPointAnchors(containerShape, ellipse);
		hook(containerShape);
		createDIShape(containerShape, e);
		updatePictogramElement(containerShape);
		return containerShape;
	}

	protected void decorateEllipse(Ellipse ellipse) {
	}

	protected void hook(ContainerShape container) {
	}
}