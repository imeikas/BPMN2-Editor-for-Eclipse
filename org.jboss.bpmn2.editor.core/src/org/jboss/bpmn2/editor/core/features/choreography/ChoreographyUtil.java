/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.bpmn2.editor.core.features.choreography;

import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyMessageLinkFeatureContainer.MESSAGE_LINK_LOCATION;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyMessageLinkFeatureContainer.MESSAGE_LINK_PROPERTY;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.ENVELOPE_HEIGHT_MODIFIER;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.ENV_H;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.ENV_W;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.ParticipantBandKind;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.IColorConstant;
import org.jboss.bpmn2.editor.core.di.DIUtils;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil.AnchorLocation;
import org.jboss.bpmn2.editor.core.utils.AnchorUtil.BoundaryAnchor;
import org.jboss.bpmn2.editor.core.utils.GraphicsUtil;
import org.jboss.bpmn2.editor.core.utils.GraphicsUtil.Envelope;
import org.jboss.bpmn2.editor.core.utils.StyleUtil;
import org.jboss.bpmn2.editor.core.utils.Tuple;

public class ChoreographyUtil {

	private static IGaService gaService = Graphiti.getGaService();
	private static IPeService peService = Graphiti.getPeService();

	public static List<ContainerShape> getParticipantBandContainerShapes(
			ContainerShape choreographyActivityContainerShape) {
		List<ContainerShape> containers = new ArrayList<ContainerShape>();
		Collection<Shape> shapes = peService.getAllContainedShapes(choreographyActivityContainerShape);
		for (Shape s : shapes) {
			String property = peService.getPropertyValue(s, ChoreographyProperties.BAND);
			if (property != null && new Boolean(property)) {
				containers.add((ContainerShape) s);
			}
		}
		return containers;
	}

	public static List<BPMNShape> getParicipantBandBpmnShapes(ContainerShape choreographyActivityContainerShape) {
		List<BPMNShape> bpmnShapes = new ArrayList<BPMNShape>();
		List<ContainerShape> containers = getParticipantBandContainerShapes(choreographyActivityContainerShape);
		for (ContainerShape container : containers) {
			BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(container, BPMNShape.class);
			bpmnShapes.add(bpmnShape);
		}
		return bpmnShapes;
	}

	public static boolean isChoreographyParticipantBand(PictogramElement element) {
		EObject container = element.eContainer();
		if (container instanceof PictogramElement) {
			PictogramElement containerElem = (PictogramElement) container;
			if (BusinessObjectUtil.containsElementOfType(containerElem, ChoreographyActivity.class)) {
				return true;
			}
		}
		return false;
	}

	public static Tuple<List<ContainerShape>, List<ContainerShape>> getTopAndBottomBands(
			List<ContainerShape> participantBands) {
		Collections.sort(participantBands, getParticipantBandComparator());
		int n = participantBands.size();
		int divider = n / 2;
		List<ContainerShape> top = participantBands.subList(0, divider);
		List<ContainerShape> bottom = participantBands.subList(divider, n);
		return new Tuple<List<ContainerShape>, List<ContainerShape>>(top, bottom);
	}

	private static Comparator<ContainerShape> getParticipantBandComparator() {
		return new Comparator<ContainerShape>() {

			@Override
			public int compare(ContainerShape c1, ContainerShape c2) {
				BPMNShape bpmnShape1 = BusinessObjectUtil.getFirstElementOfType(c1, BPMNShape.class);
				Bounds bounds1 = bpmnShape1.getBounds();
				BPMNShape bpmnShape2 = BusinessObjectUtil.getFirstElementOfType(c2, BPMNShape.class);
				Bounds bounds2 = bpmnShape2.getBounds();
				return new Float(bounds1.getY()).compareTo(new Float(bounds2.getY()));
			}

		};
	}

	public static void resizePartipantBandContainerShapes(int w, int h, List<ContainerShape> top,
			List<ContainerShape> bottom, Diagram diagram) {

		int y = 0;
		for (ContainerShape container : top) {
			BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(container, BPMNShape.class);
			Bounds bounds = bpmnShape.getBounds();
			int hAcc = (int) bounds.getHeight();
			gaService.setLocationAndSize(container.getGraphicsAlgorithm(), 0, y, w, hAcc);
			y += hAcc;
			resizeParticipantBandChildren(container, w);
			DIUtils.updateDIShape(container);
			AnchorUtil.relocateFixPointAnchors(container, w, (int) bounds.getHeight());
			AnchorUtil.reConnect(bpmnShape, diagram);
			moveParticipantBandConnections(container);
		}

		Collections.reverse(bottom); // start from bottom towards center
		y = h;
		for (ContainerShape container : bottom) {
			BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(container, BPMNShape.class);
			Bounds bounds = bpmnShape.getBounds();
			y -= bounds.getHeight();
			gaService.setLocationAndSize(container.getGraphicsAlgorithm(), 0, y, w, (int) bounds.getHeight());
			resizeParticipantBandChildren(container, w);
			DIUtils.updateDIShape(container);
			AnchorUtil.relocateFixPointAnchors(container, w, (int) bounds.getHeight());
			AnchorUtil.reConnect(bpmnShape, diagram);
			moveParticipantBandConnections(container);
		}
	}

	private static void resizeParticipantBandChildren(ContainerShape container, int w) {
		for (Shape s : container.getChildren()) {
			GraphicsAlgorithm ga = s.getGraphicsAlgorithm();
			if (ga instanceof Text) {
				gaService.setSize(ga, w, ga.getHeight());
			} else if (ga instanceof Rectangle) {
				gaService.setLocation(ga, (w / 2) - (ga.getWidth() / 2), ga.getY());
			}
		}
	}

	public static void moveParticipantBandConnections(ContainerShape participantBandContainer, int dx, int dy) {
		List<Connection> connections = Graphiti.getPeService().getOutgoingConnections(participantBandContainer);
		for (Connection connection : connections) {
			ContainerShape envelope = (ContainerShape) connection.getEnd().getParent();
			GraphicsAlgorithm envelopeGa = envelope.getGraphicsAlgorithm();
			Graphiti.getGaService().setLocation(envelopeGa, envelopeGa.getX() + dx, envelopeGa.getY() + dy);
		}
	}

	public static void moveParticipantBandConnections(ContainerShape participantBandContainer) {
		BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(participantBandContainer, BPMNShape.class);
		Bounds bounds = bpmnShape.getBounds();

		List<Connection> connections = peService.getOutgoingConnections(participantBandContainer);
		for (Connection connection : connections) {
			ContainerShape envelope = (ContainerShape) connection.getEnd().getParent();
			AnchorLocation location = AnchorLocation.valueOf(peService
					.getPropertyValue(envelope, MESSAGE_LINK_LOCATION));
			GraphicsAlgorithm envelopeGa = envelope.getGraphicsAlgorithm();

			int newX = (int) (bounds.getX() + ((bounds.getWidth() / 2) - (envelopeGa.getWidth() / 2)));
			int newY = (int) (location == AnchorLocation.BOTTOM ? bounds.getY() - ENVELOPE_HEIGHT_MODIFIER - ENV_H
					: bounds.getY() + bounds.getHeight() + ENVELOPE_HEIGHT_MODIFIER);

			gaService.setLocation(envelopeGa, newX, newY);
		}
	}

	public static String getParticipantRefIds(ChoreographyActivity choreography) {
		Iterator<Participant> iterator = choreography.getParticipantRefs().iterator();
		String delim = ":";
		StringBuilder sb = new StringBuilder();
		while (iterator.hasNext()) {
			Participant participant = (Participant) iterator.next();
			sb.append(participant.getId());
			if (iterator.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	public static void updateParticipantReferences(ContainerShape choreographyContainer,
			List<ContainerShape> currentParticipantContainers, List<Participant> newParticipants, IFeatureProvider fp) {

		Diagram diagram = peService.getDiagramForShape(choreographyContainer);
		ChoreographyActivity choreography = BusinessObjectUtil.getFirstElementOfType(choreographyContainer,
				ChoreographyActivity.class);

		BPMNDiagram dia = BusinessObjectUtil.getFirstElementOfType(diagram, BPMNDiagram.class);
		List<DiagramElement> diElements = dia.getPlane().getPlaneElement();
		for (int i = 0; i < currentParticipantContainers.size(); i++) {
			ContainerShape container = currentParticipantContainers.get(i);
			for (Connection c : peService.getOutgoingConnections(container)) {
				EObject parent = c.getEnd().eContainer();
				if (parent instanceof PictogramElement) {
					peService.deletePictogramElement((PictogramElement) parent);
				}
			}
			BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(container, BPMNShape.class);
			diElements.remove(bpmnShape);
			peService.deletePictogramElement(container);
		}

		GraphicsAlgorithm ga = choreographyContainer.getGraphicsAlgorithm();
		IDimension size = gaService.calculateSize(ga);

		List<ContainerShape> newContainers = new ArrayList<ContainerShape>();
		int y = 0;
		boolean first = true;

		Iterator<Participant> iterator = newParticipants.iterator();
		while (iterator.hasNext()) {
			Participant participant = (Participant) iterator.next();

			ContainerShape bandShape = peService.createContainerShape(choreographyContainer, true);

			ParticipantBandKind bandKind = getNewParticipantBandKind(choreography, participant, first,
					!iterator.hasNext());

			boolean multiple = participant.getParticipantMultiplicity() != null
					&& participant.getParticipantMultiplicity().getMaximum() > 1;

			int w = size.getWidth();
			int h = multiple ? 40 : 20;

			BPMNShape bpmnShape = DIUtils.createDIShape(bandShape, participant, 0, y + h, w, h, fp, diagram);
			bpmnShape.setChoreographyActivityShape(BusinessObjectUtil.getFirstElementOfType(choreographyContainer,
					BPMNShape.class));
			bpmnShape.setIsMarkerVisible(multiple);
			bpmnShape.setParticipantBandKind(bandKind);
			createParticipantBandContainerShape(bandKind, choreographyContainer, bandShape, bpmnShape);
			if (multiple) {
				drawMultiplicityMarkers(bandShape);
			}
			newContainers.add(bandShape);

			y += h;
			first = false;
		}

		Tuple<List<ContainerShape>, List<ContainerShape>> topAndBottom = getTopAndBottomBands(newContainers);
		resizePartipantBandContainerShapes(size.getWidth(), size.getHeight(), topAndBottom.getFirst(),
				topAndBottom.getSecond(), diagram);
	}

	public static ContainerShape createTopShape(ContainerShape parent, ContainerShape bandShape, BPMNShape bpmnShape,
			boolean initiating) {

		if (bandShape == null) {
			bandShape = peService.createContainerShape(parent, true);
		}

		Bounds bounds = bpmnShape.getBounds();
		int w = (int) bounds.getWidth();
		int h = (int) bounds.getHeight();

		Diagram diagram = peService.getDiagramForPictogramElement(parent);
		RoundedRectangle band = gaService.createRoundedRectangle(bandShape, R, R);
		band.setForeground(gaService.manageColor(diagram, StyleUtil.CLASS_FOREGROUND));
		band.setBackground(initiating ? gaService.manageColor(diagram, IColorConstant.WHITE) : gaService.manageColor(
				diagram, IColorConstant.LIGHT_GRAY));
		gaService.setLocationAndSize(band, 0, 0, w, h);

		Participant p = (Participant) bpmnShape.getBpmnElement();
		addBandLabel(bandShape, p.getName(), w, h);
		AnchorUtil.addFixedPointAnchors(bandShape, band);
		peService.setPropertyValue(bandShape, ChoreographyProperties.BAND, Boolean.toString(true));
		return bandShape;
	}

	public static ContainerShape createBottomShape(ContainerShape parent, ContainerShape bandShape,
			BPMNShape bpmnShape, boolean initiating) {

		if (bandShape == null) {
			bandShape = peService.createContainerShape(parent, true);
		}

		Bounds bounds = bpmnShape.getBounds();
		int w = (int) bounds.getWidth();
		int h = (int) bounds.getHeight();

		ILocation parentLoc = peService.getLocationRelativeToDiagram(parent);
		int y = (int) bounds.getY() - parentLoc.getY();

		Diagram diagram = peService.getDiagramForPictogramElement(parent);
		RoundedRectangle band = gaService.createRoundedRectangle(bandShape, R, R);
		band.setForeground(gaService.manageColor(diagram, StyleUtil.CLASS_FOREGROUND));
		band.setBackground(initiating ? gaService.manageColor(diagram, IColorConstant.WHITE) : gaService.manageColor(
				diagram, IColorConstant.LIGHT_GRAY));
		gaService.setLocationAndSize(band, 0, y, w, h);

		Participant p = (Participant) bpmnShape.getBpmnElement();
		addBandLabel(bandShape, p.getName(), w, h);
		AnchorUtil.addFixedPointAnchors(bandShape, band);
		peService.setPropertyValue(bandShape, ChoreographyProperties.BAND, Boolean.toString(true));
		return bandShape;
	}

	public static ContainerShape createMiddleShape(ContainerShape parent, ContainerShape bandShape,
			BPMNShape bpmnShape, boolean initiating) {

		if (bandShape == null) {
			bandShape = peService.createContainerShape(parent, true);
		}

		Bounds bounds = bpmnShape.getBounds();
		int w = (int) bounds.getWidth();
		int h = (int) bounds.getHeight();

		ILocation parentLoc = peService.getLocationRelativeToDiagram(parent);
		int y = (int) bounds.getY() - parentLoc.getY();

		Diagram diagram = peService.getDiagramForPictogramElement(parent);
		Rectangle band = gaService.createRectangle(bandShape);
		band.setForeground(gaService.manageColor(diagram, StyleUtil.CLASS_FOREGROUND));
		band.setBackground(initiating ? gaService.manageColor(diagram, IColorConstant.WHITE) : gaService.manageColor(
				diagram, IColorConstant.LIGHT_GRAY));
		gaService.setLocationAndSize(band, 0, y, w, h);

		Participant p = (Participant) bpmnShape.getBpmnElement();
		addBandLabel(bandShape, p.getName(), w, h);
		AnchorUtil.addFixedPointAnchors(bandShape, band);
		peService.setPropertyValue(bandShape, ChoreographyProperties.BAND, Boolean.toString(true));
		return bandShape;
	}

	private static void addBandLabel(ContainerShape container, String name, int w, int h) {
		Shape labelShape = peService.createShape(container, false);
		Text label = gaService.createDefaultText(labelShape);
		label.setValue(name);
		gaService.setLocationAndSize(label, 0, 0, w, h);
		label.setStyle(StyleUtil.getStyleForText(peService.getDiagramForPictogramElement(container)));
		label.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		label.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
	}

	private static ParticipantBandKind getNewParticipantBandKind(ChoreographyActivity choreography,
			Participant participant, boolean first, boolean last) {
		boolean initiating = choreography.getInitiatingParticipantRef().equals(participant);
		if (first) {
			return initiating ? ParticipantBandKind.TOP_INITIATING : ParticipantBandKind.TOP_NON_INITIATING;
		} else if (last) {
			return initiating ? ParticipantBandKind.BOTTOM_INITIATING : ParticipantBandKind.BOTTOM_NON_INITIATING;
		} else {
			return initiating ? ParticipantBandKind.MIDDLE_INITIATING : ParticipantBandKind.MIDDLE_NON_INITIATING;
		}
	}

	public static void drawMessageLink(BoundaryAnchor boundaryAnchor, int x, int y, boolean filled) {
		Diagram diagram = peService.getDiagramForAnchor(boundaryAnchor.anchor);

		FreeFormConnection connection = peService.createFreeFormConnection(diagram);
		Polyline connectionLine = gaService.createPolyline(connection);
		connectionLine.setForeground(gaService.manageColor(diagram, StyleUtil.CLASS_FOREGROUND));
		connectionLine.setLineStyle(LineStyle.DOT);
		connectionLine.setLineWidth(2);

		ContainerShape envelope = peService.createContainerShape(diagram, true);
		Envelope envelopeGa = GraphicsUtil.createEnvelope(envelope, x, y, ENV_W, ENV_H);
		IColorConstant color = filled ? IColorConstant.LIGHT_GRAY : IColorConstant.WHITE;
		envelopeGa.rect.setFilled(true);
		envelopeGa.rect.setBackground(gaService.manageColor(diagram, color));
		envelopeGa.rect.setForeground(gaService.manageColor(diagram, StyleUtil.CLASS_FOREGROUND));
		envelopeGa.line.setForeground(gaService.manageColor(diagram, StyleUtil.CLASS_FOREGROUND));
		AnchorUtil.addFixedPointAnchors(envelope, envelopeGa.rect);

		AnchorLocation envelopeAnchorLoc = null;
		if (boundaryAnchor.locationType == AnchorLocation.TOP) {
			envelopeAnchorLoc = AnchorLocation.BOTTOM;
		} else {
			envelopeAnchorLoc = AnchorLocation.TOP;
		}

		connection.setStart(boundaryAnchor.anchor);
		connection.setEnd(AnchorUtil.getBoundaryAnchors(envelope).get(envelopeAnchorLoc).anchor);
		peService.setPropertyValue(envelope, MESSAGE_LINK_PROPERTY, Boolean.toString(true));
		peService.setPropertyValue(envelope, MESSAGE_LINK_LOCATION, envelopeAnchorLoc.toString());
	}

	public static void drawMultiplicityMarkers(ContainerShape container) {
		Diagram diagram = peService.getDiagramForPictogramElement(container);
		Shape multiplicityShape = peService.createShape(container, false);
		Rectangle rect = gaService.createInvisibleRectangle(multiplicityShape);

		IDimension size = gaService.calculateSize(container.getGraphicsAlgorithm());
		int w = 10;
		int h = 10;
		int x = (size.getWidth() / 2) - (w / 2);
		int y = size.getHeight() - h - 1;
		gaService.setLocationAndSize(rect, x, y, w, h);

		int[][] coorinates = { new int[] { 0, 0, 0, h }, new int[] { 4, 0, 4, h }, new int[] { 8, 0, 8, h } };
		for (int[] xy : coorinates) {
			Polyline line = gaService.createPolyline(rect, xy);
			line.setLineWidth(2);
			line.setForeground(gaService.manageColor(diagram, StyleUtil.CLASS_FOREGROUND));
		}
	}

	public static ContainerShape createParticipantBandContainerShape(ParticipantBandKind bandKind,
			ContainerShape container, ContainerShape bandContainer, BPMNShape bpmnShape) {

		switch (bandKind) {
		case TOP_INITIATING:
			return createTopShape(container, bandContainer, bpmnShape, true);
		case TOP_NON_INITIATING:
			return createTopShape(container, bandContainer, bpmnShape, false);
		case MIDDLE_INITIATING:
			return createMiddleShape(container, bandContainer, bpmnShape, true);
		case MIDDLE_NON_INITIATING:
			return createMiddleShape(container, bandContainer, bpmnShape, false);
		case BOTTOM_INITIATING:
			return createBottomShape(container, bandContainer, bpmnShape, true);
		case BOTTOM_NON_INITIATING:
			return createBottomShape(container, bandContainer, bpmnShape, false);
		}

		return bandContainer;
	}

	public static ContainerShape createParticipantBandContainerShape(ParticipantBandKind bandKind,
			ContainerShape container, BPMNShape bpmnShape) {
		return createParticipantBandContainerShape(bandKind, container, null, bpmnShape);
	}
}