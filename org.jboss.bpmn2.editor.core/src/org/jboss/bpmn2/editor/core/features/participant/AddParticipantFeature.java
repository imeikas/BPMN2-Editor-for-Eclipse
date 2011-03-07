package org.jboss.bpmn2.editor.core.features.participant;

import org.eclipse.bpmn2.Participant;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.jboss.bpmn2.editor.core.features.AbstractBpmnAddFeature;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil;
import org.jboss.bpmn2.editor.core.utils.StyleUtil;

public class AddParticipantFeature extends AbstractBpmnAddFeature {

	public AddParticipantFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean isParticipant = context.getNewObject() instanceof Participant;
		boolean addToDiagram = context.getTargetContainer() instanceof Diagram;
		return isParticipant && addToDiagram;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		Participant p = (Participant) context.getNewObject();

		Diagram targetDiagram = (Diagram) context.getTargetContainer();
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(targetDiagram, true);
		IGaService gaService = Graphiti.getGaService();

		int width = context.getWidth() > 0 ? context.getWidth() : 600;
		int height = context.getHeight() > 0 ? context.getHeight() : 100;

		Rectangle rect = gaService.createRectangle(containerShape);
		
		StyleUtil.applyBGStyle(rect, this);
		
		gaService.setLocationAndSize(rect, context.getX(), context.getY(), width, height);

		Shape lineShape = peCreateService.createShape(containerShape, false);
		Polyline line = gaService.createPolyline(lineShape, new int[] { 15, 0, 15, height });
		line.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));

		Shape textShape = peCreateService.createShape(containerShape, false);
		Text text = gaService.createText(textShape, p.getName());
		text.setStyle(StyleUtil.getStyleForText(getDiagram()));
		text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setAngle(-90);
		gaService.setLocationAndSize(text, 0, 0, 15, height);

		createDIShape(containerShape, p);
		link(textShape, p);

		peCreateService.createChopboxAnchor(containerShape);
		AnchorUtil.addFixedPointAnchors(containerShape, rect);
		
		return containerShape;
	}
}
