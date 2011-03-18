package org.jboss.bpmn2.editor.core.features.choreography;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.ParticipantBandKind;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.IColorConstant;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.di.DIImport;
import org.jboss.bpmn2.editor.core.features.AbstractBpmnAddFeature;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil;
import org.jboss.bpmn2.editor.core.utils.StyleUtil;

public class AddChoreographyFeature extends AbstractBpmnAddFeature {

	protected final IGaService gaService = Graphiti.getGaService();
	protected final IPeService peService = Graphiti.getPeService();

	public AddChoreographyFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context.getTargetContainer().equals(getDiagram());
	}

	@Override
	public PictogramElement add(IAddContext context) {
		ChoreographyActivity choreography = (ChoreographyActivity) context.getNewObject();

		int width = context.getWidth() > 0 ? context.getWidth() : 100;
		int height = context.getHeight() > 0 ? context.getHeight() : 100;

		ContainerShape containerShape = peService.createContainerShape(context.getTargetContainer(), true);
		RoundedRectangle containerRect = gaService.createRoundedRectangle(containerShape, 10, 10);
		gaService.setLocationAndSize(containerRect, context.getX(), context.getY(), width, height);
		StyleUtil.applyBGStyle(containerRect, this);
		decorateContainerRect(containerRect);

		Object importProperty = context.getProperty(DIImport.IMPORT_PROPERTY);
		if (importProperty != null && (Boolean) importProperty) {
			addedFromImport(choreography, containerShape, context);
		} else {
			addedByUser(context);
		}

		link(containerShape, choreography);
		peService.createChopboxAnchor(containerShape);
		AnchorUtil.addFixedPointAnchors(containerShape, containerRect);
		createDIShape(containerShape, choreography);
		return containerShape;
	}

	protected void decorateContainerRect(RoundedRectangle containerRect) {
	}

	protected void addedFromImport(ChoreographyActivity choreography, ContainerShape container, IAddContext context) {
		ModelHandler mh = null;

		try {
			mh = ModelHandler.getInstance(getDiagram());
		} catch (IOException e) {
			Activator.logError(e);
			return;
		}

		List<Participant> participants = choreography.getParticipantRefs();
		List<BPMNShape> shapes = mh.getAll(BPMNShape.class);
		List<BPMNShape> filteredShapes = new ArrayList<BPMNShape>();
		BPMNShape choreoBpmnShape = null;

		for (BPMNShape shape : shapes) {
			if (shape.getBpmnElement().equals(choreography)) {
				choreoBpmnShape = shape;
				break;
			}
		}

		for (BPMNShape shape : shapes) {
			if (participants.contains(shape.getBpmnElement())
			        && choreoBpmnShape.equals(shape.getChoreographyActivityShape())) {
				filteredShapes.add(shape);
			}
		}

		for (BPMNShape shape : filteredShapes) {
			ParticipantBandKind bandKind = shape.getParticipantBandKind();
			Shape createdShape = null;
			switch (bandKind) {
			case TOP_INITIATING:
				createdShape = createTopShape(container, shape, true);
				break;
			case TOP_NON_INITIATING:
				createdShape = createTopShape(container, shape, false);
				break;
			case MIDDLE_INITIATING:
				createdShape = createMiddleShape(container, shape, true);
				break;
			case MIDDLE_NON_INITIATING:
				createdShape = createMiddleShape(container, shape, false);
				break;
			case BOTTOM_INITIATING:
				createdShape = createBottomShape(container, shape, true);
				break;
			case BOTTOM_NON_INITIATING:
				createdShape = createBottomShape(container, shape, false);
				break;
			}
			link(createdShape, shape.getBpmnElement());
		}
	}

	private Shape createTopShape(ContainerShape parent, BPMNShape shape, boolean initiating) {
		Shape bandShape = peService.createShape(parent, true);

		Bounds bounds = shape.getBounds();
		int w = (int) bounds.getWidth();
		int h = (int) bounds.getHeight();
		int[] xy = { 0, h, 0, 0, w, 0, w, h };
		int[] beforeAfter = { 0, 0, 10, 10, 10, 10, 0, 0 };

		Polygon band = gaService.createPolygon(bandShape, xy, beforeAfter);
		band.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		band.setBackground(initiating ? manageColor(IColorConstant.WHITE) : manageColor(IColorConstant.LIGHT_GRAY));

		return bandShape;
	}

	private Shape createBottomShape(ContainerShape parent, BPMNShape shape, boolean initiating) {
		Shape bandShape = peService.createShape(parent, true);

		Bounds bounds = shape.getBounds();
		int w = (int) bounds.getWidth();
		int h = (int) bounds.getHeight();

		ILocation parentLoc = peService.getLocationRelativeToDiagram(parent);
		int y = (int) bounds.getY() - parentLoc.getY();
		System.out.println(y);

		int[] xy = { 0, y, w, y, w, y + h, 0, y + h };
		int[] beforeAfter = { 0, 0, 0, 0, 10, 10, 10, 10 };

		Polygon band = gaService.createPolygon(bandShape, xy, beforeAfter);
		band.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		band.setBackground(initiating ? manageColor(IColorConstant.WHITE) : manageColor(IColorConstant.LIGHT_GRAY));

		return bandShape;
	}

	private Shape createMiddleShape(ContainerShape parent, BPMNShape shape, boolean initiating) {
		Shape bandShape = peService.createShape(parent, true);

		Bounds bounds = shape.getBounds();
		int w = (int) bounds.getWidth();
		int h = (int) bounds.getHeight();

		ILocation parentLoc = peService.getLocationRelativeToDiagram(parent);
		int y = (int) bounds.getY() - parentLoc.getY();
		System.out.println(y);

		Rectangle band = gaService.createRectangle(bandShape);
		band.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		band.setBackground(initiating ? manageColor(IColorConstant.WHITE) : manageColor(IColorConstant.LIGHT_GRAY));
		gaService.setLocationAndSize(band, 0, y, w, h);

		return bandShape;
	}

	protected void addedByUser(IAddContext context) {
	}
}