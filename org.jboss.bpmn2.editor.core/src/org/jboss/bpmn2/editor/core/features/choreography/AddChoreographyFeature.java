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
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
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
import org.jboss.bpmn2.editor.core.utils.AnchorUtil.AnchorLocation;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil.BoundaryAnchor;
import org.jboss.bpmn2.editor.core.utils.GraphicsUtil;
import org.jboss.bpmn2.editor.core.utils.GraphicsUtil.Envelope;
import org.jboss.bpmn2.editor.core.utils.StyleUtil;

public class AddChoreographyFeature extends AbstractBpmnAddFeature {

	private static final int R = 10;
	private static final int ENV_W = 30;
	private static final int ENV_H = 18;

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
		RoundedRectangle containerRect = gaService.createRoundedRectangle(containerShape, R, R);
		gaService.setLocationAndSize(containerRect, context.getX(), context.getY(), width, height);
		StyleUtil.applyBGStyle(containerRect, this);
		decorateContainerRect(containerRect);

		Object importProperty = context.getProperty(DIImport.IMPORT_PROPERTY);
		if (importProperty != null && (Boolean) importProperty) {
			addedFromImport(choreography, containerShape, context);
		} else {
			addedByUser(context);
		}

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

		for (BPMNShape bpmnShape : filteredShapes) {
			ParticipantBandKind bandKind = bpmnShape.getParticipantBandKind();
			Shape createdShape = null;
			boolean top = false;
			switch (bandKind) {
			case TOP_INITIATING:
				createdShape = createTopShape(container, bpmnShape, true);
				top = true;
				break;
			case TOP_NON_INITIATING:
				createdShape = createTopShape(container, bpmnShape, false);
				top = true;
				break;
			case MIDDLE_INITIATING:
				createdShape = createMiddleShape(container, bpmnShape, true);
				break;
			case MIDDLE_NON_INITIATING:
				createdShape = createMiddleShape(container, bpmnShape, false);
				break;
			case BOTTOM_INITIATING:
				createdShape = createBottomShape(container, bpmnShape, true);
				break;
			case BOTTOM_NON_INITIATING:
				createdShape = createBottomShape(container, bpmnShape, false);
				break;
			}
			createDIShape(createdShape, bpmnShape.getBpmnElement(), bpmnShape);
			AnchorUtil.addFixedPointAnchors(createdShape, createdShape.getGraphicsAlgorithm());

			if (bpmnShape.isIsMessageVisible()) {
				BoundaryAnchor anchor = AnchorUtil.getBoundaryAnchors(createdShape).get(
						top ? AnchorLocation.TOP : AnchorLocation.BOTTOM);
				Bounds bounds = bpmnShape.getBounds();
				int x = (int) (bounds.getX() + bounds.getWidth() / 2) - ENV_W / 2;
				int y = (int) (top ? bounds.getY() - 30 - ENV_H : bounds.getY() + bounds.getHeight() + 30);
				boolean filled = bandKind == ParticipantBandKind.TOP_NON_INITIATING
						|| bandKind == ParticipantBandKind.BOTTOM_NON_INITIATING;
				drawMessageLink(anchor, x, y, filled);
			}
		}
	}

	private Shape createTopShape(ContainerShape parent, BPMNShape shape, boolean initiating) {
		Shape bandShape = peService.createShape(parent, true);

		Bounds bounds = shape.getBounds();
		int w = (int) bounds.getWidth();
		int h = (int) bounds.getHeight();
		int[] xy = { 0, h, 0, 0, w, 0, w, h };
		int[] beforeAfter = { 0, 0, R, R, R, R, R, R };

		Polygon band = gaService.createPolygon(bandShape, xy, beforeAfter);
		band.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		band.setBackground(initiating ? manageColor(IColorConstant.WHITE) : manageColor(IColorConstant.LIGHT_GRAY));

		Participant p = (Participant) shape.getBpmnElement();
		addBandLabel(band, p.getName(), w, h);
		return bandShape;
	}

	private Shape createBottomShape(ContainerShape parent, BPMNShape shape, boolean initiating) {
		Shape bandShape = peService.createShape(parent, true);

		Bounds bounds = shape.getBounds();
		int w = (int) bounds.getWidth();
		int h = (int) bounds.getHeight();

		ILocation parentLoc = peService.getLocationRelativeToDiagram(parent);
		int y = (int) bounds.getY() - parentLoc.getY();

		int[] xy = { 0, y, w, y, w, y + h, 0, y + h };
		int[] beforeAfter = { 0, 0, 0, 0, R, R, R, R };

		Polygon band = gaService.createPolygon(bandShape, xy, beforeAfter);
		band.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		band.setBackground(initiating ? manageColor(IColorConstant.WHITE) : manageColor(IColorConstant.LIGHT_GRAY));

		Participant p = (Participant) shape.getBpmnElement();
		addBandLabel(band, p.getName(), w, h);
		return bandShape;
	}

	private Shape createMiddleShape(ContainerShape parent, BPMNShape shape, boolean initiating) {
		Shape bandShape = peService.createShape(parent, true);

		Bounds bounds = shape.getBounds();
		int w = (int) bounds.getWidth();
		int h = (int) bounds.getHeight();

		ILocation parentLoc = peService.getLocationRelativeToDiagram(parent);
		int y = (int) bounds.getY() - parentLoc.getY();

		Rectangle band = gaService.createRectangle(bandShape);
		band.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		band.setBackground(initiating ? manageColor(IColorConstant.WHITE) : manageColor(IColorConstant.LIGHT_GRAY));
		gaService.setLocationAndSize(band, 0, y, w, h);

		Participant p = (Participant) shape.getBpmnElement();
		addBandLabel(band, p.getName(), w, h);
		return bandShape;
	}

	private void addBandLabel(GraphicsAlgorithm ga, String name, int w, int h) {
		Text label = gaService.createDefaultText(ga);
		label.setValue(name);
		gaService.setLocationAndSize(label, 0, 0, w, h);
		label.setStyle(StyleUtil.getStyleForText(getDiagram()));
		label.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		label.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
	}

	private void drawMessageLink(BoundaryAnchor boundaryAnchor, int x, int y, boolean filled) {
		FreeFormConnection connection = peService.createFreeFormConnection(getDiagram());
		Polyline connectionLine = gaService.createPolyline(connection);
		connectionLine.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		connectionLine.setLineStyle(LineStyle.DOT);
		connectionLine.setLineWidth(2);

		ContainerShape envelope = peService.createContainerShape(getDiagram(), true);
		Envelope envelopeGa = GraphicsUtil.createEnvelope(envelope, x, y, ENV_W, ENV_H);
		IColorConstant color = filled ? IColorConstant.LIGHT_GRAY : IColorConstant.WHITE;
		envelopeGa.rect.setFilled(true);
		envelopeGa.rect.setBackground(manageColor(color));
		envelopeGa.rect.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		envelopeGa.line.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		AnchorUtil.addFixedPointAnchors(envelope, envelopeGa.rect);

		AnchorLocation envelopeAnchorLoc = null;
		if (boundaryAnchor.locationType == AnchorLocation.TOP) {
			envelopeAnchorLoc = AnchorLocation.BOTTOM;
		} else {
			envelopeAnchorLoc = AnchorLocation.TOP;
		}

		connection.setStart(boundaryAnchor.anchor);
		connection.setEnd(AnchorUtil.getBoundaryAnchors(envelope).get(envelopeAnchorLoc).anchor);
	}

	protected void addedByUser(IAddContext context) {
	}
}