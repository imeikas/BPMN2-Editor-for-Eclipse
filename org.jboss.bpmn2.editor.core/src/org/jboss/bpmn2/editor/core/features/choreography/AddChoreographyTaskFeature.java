package org.jboss.bpmn2.editor.core.features.choreography;

import static org.jboss.bpmn2.editor.core.features.choreography.Properties.*;
import org.eclipse.bpmn2.ChoreographyTask;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.IColorConstant;
import org.jboss.bpmn2.editor.core.features.AbstractBpmnAddFeature;
import org.jboss.bpmn2.editor.utils.AnchorUtil;
import org.jboss.bpmn2.editor.utils.StyleUtil;

public class AddChoreographyTaskFeature extends AbstractBpmnAddFeature {

	public static final int PARTICIPANT_BAND_HEIGHT = 25;
	private static final IGaService gaService = Graphiti.getGaService();
	private static final IPeService peService = Graphiti.getPeService();

	public AddChoreographyTaskFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context.getTargetContainer().equals(getDiagram());
	}

	@Override
	public PictogramElement add(IAddContext context) {
		ChoreographyTask task = (ChoreographyTask) context.getNewObject();

		int width = context.getWidth() > 0 ? context.getWidth() : 100;
		int height = context.getHeight() > 0 ? context.getHeight() : 100;

		ContainerShape containerShape = peService.createContainerShape(context.getTargetContainer(), true);
		Rectangle invisibleRect = gaService.createInvisibleRectangle(containerShape);
		gaService.setLocationAndSize(invisibleRect, context.getX(), context.getY(), width, height);

		int rectH = PARTICIPANT_BAND_HEIGHT;

		Shape topRectShape = peService.createShape(containerShape, false);
		RoundedRectangle topRect = gaService.createRoundedRectangle(topRectShape, 5, 5);
		gaService.setLocationAndSize(topRect, 0, 0, width, rectH);
		topRect.setFilled(true);
		topRect.setBackground(manageColor(IColorConstant.LIGHT_GRAY));
		topRect.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		peService.setPropertyValue(topRectShape, CHOREOGRAPHY_TASK_PROPERTY, TOP_BAND);

		createText(containerShape, "Participant A", 0, 0, width, rectH, TOP_BAND_TEXT);

		Shape bottomRectShape = peService.createShape(containerShape, false);
		RoundedRectangle bottomRect = gaService.createRoundedRectangle(bottomRectShape, 5, 5);
		gaService.setLocationAndSize(bottomRect, 0, height - rectH, width, rectH);
		bottomRect.setFilled(true);
		bottomRect.setBackground(manageColor(IColorConstant.LIGHT_GRAY));
		bottomRect.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		peService.setPropertyValue(bottomRectShape, CHOREOGRAPHY_TASK_PROPERTY, BOTTOM_BAND);

		createText(containerShape, "Participant B", 0, height - rectH, width, rectH, BOTTOM_BAND_TEXT);

		Shape bodyShape = peService.createShape(containerShape, false);
		Rectangle body = gaService.createRectangle(bodyShape);
		gaService.setLocationAndSize(body, 0, rectH - 5, width, height - (2 * rectH) + 10);
		StyleUtil.applyBGStyle(body, this);
		peService.setPropertyValue(bodyShape, CHOREOGRAPHY_TASK_PROPERTY, BODY_BAND);

		createText(containerShape, "Task Name", 0, rectH - 5, width, height - (2 * rectH) + 10, BODY_BAND_TEXT);

		peService.setPropertyValue(containerShape, PARTICIPANT_REF_NUM, Integer.toString(0));
		peService.setPropertyValue(containerShape, INITIATING_PARTICIPANT_REF, Boolean.toString(false));
		
		link(containerShape, task);
		peService.createChopboxAnchor(containerShape);
		AnchorUtil.addFixedPointAnchors(containerShape, invisibleRect);
		layoutPictogramElement(containerShape);
		updatePictogramElement(containerShape);
		createDIShape(containerShape, task);
		return containerShape;
	}

	private Text createText(ContainerShape container, String text, int x, int y, int w, int h, String propertyValue) {
		Shape shape = peService.createShape(container, false);
		Text bodyText = gaService.createText(shape);
		gaService.setLocationAndSize(bodyText, x, y, w, h);
		bodyText.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		bodyText.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
		peService.setPropertyValue(shape, CHOREOGRAPHY_TASK_PROPERTY, propertyValue);
		bodyText.setStyle(StyleUtil.getStyleForText(getDiagram()));
		bodyText.setValue(text);
		return bodyText;
	}
}