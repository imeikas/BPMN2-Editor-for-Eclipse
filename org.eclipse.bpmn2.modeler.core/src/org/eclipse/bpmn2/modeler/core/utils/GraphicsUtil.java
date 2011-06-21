/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class GraphicsUtil {

	private static final IGaService gaService = Graphiti.getGaService();
	private static final IPeService peService = Graphiti.getPeService();

	// TODO move all size properties to separate interface
	public static int DATA_WIDTH = 36;
	public static int DATA_HEIGHT = 50;

	public static class Envelope {
		public Rectangle rect;
		public Polyline line;
	}

	public static class Asterisk {
		public Polyline horizontal;
		public Polyline vertical;
		public Polyline diagonalDesc;
		public Polyline diagonalAsc;
	}

	public static class Compensation {
		public Polygon arrow1;
		public Polygon arrow2;
	}

	public static class Cross {
		public Polyline vertical;
		public Polyline horizontal;
	}

	public static class MultiInstance {
		public Polyline line1;
		public Polyline line2;
		public Polyline line3;
	}

	public static class Loop {
		public Polyline circle;
		public Polyline arrow;
	}

	public static class Expand {
		public Rectangle rect;
		public Polyline horizontal;
		public Polyline vertical;
	}

	/* GATEWAY */

	private static final String DELETABLE_PROPERTY = "deletable";

	public static final int GATEWAY_RADIUS = 25;
	public static final int GATEWAY_TEXT_AREA = 15;

	private static final int[] GATEWAY = { 0, GATEWAY_RADIUS, GATEWAY_RADIUS, 0, 2 * GATEWAY_RADIUS, GATEWAY_RADIUS,
	        GATEWAY_RADIUS, 2 * GATEWAY_RADIUS };

	public static Polygon createGateway(Shape container) {
		return gaService.createPolygon(container, GATEWAY);
	}

	public static Polygon createGatewayPentagon(ContainerShape container) {
		Shape pentagonShape = peService.createShape(container, false);
		Polygon pentagon = gaService.createPolygon(pentagonShape, new int[] { GATEWAY_RADIUS, 18, GATEWAY_RADIUS + 8,
		        GATEWAY_RADIUS - 2, GATEWAY_RADIUS + 5, GATEWAY_RADIUS + 7, GATEWAY_RADIUS - 5, GATEWAY_RADIUS + 7,
		        GATEWAY_RADIUS - 8, GATEWAY_RADIUS - 2 });
		peService.setPropertyValue(pentagonShape, DELETABLE_PROPERTY, "true");
		return pentagon;
	}

	public static Ellipse createGatewayInnerCircle(ContainerShape container) {
		Shape ellipseShape = peService.createShape(container, false);
		Ellipse ellipse = gaService.createEllipse(ellipseShape);
		gaService.setLocationAndSize(ellipse, 14, 14, 23, 23);
		ellipse.setFilled(false);
		ellipse.setLineWidth(1);
		peService.setPropertyValue(ellipseShape, DELETABLE_PROPERTY, "true");
		return ellipse;
	}

	public static Ellipse createGatewayOuterCircle(ContainerShape container) {
		Shape ellipseShape = peService.createShape(container, false);
		Ellipse ellipse = gaService.createEllipse(ellipseShape);
		gaService.setLocationAndSize(ellipse, 12, 12, 27, 27);
		ellipse.setFilled(false);
		ellipse.setLineWidth(1);
		peService.setPropertyValue(ellipseShape, DELETABLE_PROPERTY, "true");
		return ellipse;
	}

	public static Cross createGatewayCross(ContainerShape container) {
		Shape verticalShape = peService.createShape(container, false);
		Polyline verticalLine = gaService.createPolyline(verticalShape, new int[] { 24, 7, 24, 43 });
		verticalLine.setLineWidth(3);
		peService.setPropertyValue(verticalShape, DELETABLE_PROPERTY, "false");

		Shape horizontalShape = peService.createShape(container, false);
		Polyline horizontalLine = gaService.createPolyline(horizontalShape, new int[] { 7, 24, 43, 24 });
		horizontalLine.setLineWidth(3);
		peService.setPropertyValue(horizontalShape, DELETABLE_PROPERTY, "false");

		Cross cross = new Cross();
		cross.vertical = verticalLine;
		cross.horizontal = horizontalLine;
		return cross;
	}

	public static Polygon createEventGatewayParallelCross(ContainerShape container) {
		Shape crossShape = peService.createShape(container, false);
		int n1 = 14;
		int n2 = 22;
		int n3 = 28;
		int n4 = 36;
		Collection<Point> points = new ArrayList<Point>();
		points.add(gaService.createPoint(n1, n2));
		points.add(gaService.createPoint(n2, n2));
		points.add(gaService.createPoint(n2, n1));
		points.add(gaService.createPoint(n3, n1));
		points.add(gaService.createPoint(n3, n2));
		points.add(gaService.createPoint(n4, n2));
		points.add(gaService.createPoint(n4, n3));
		points.add(gaService.createPoint(n3, n3));
		points.add(gaService.createPoint(n3, n4));
		points.add(gaService.createPoint(n2, n4));
		points.add(gaService.createPoint(n2, n3));
		points.add(gaService.createPoint(n1, n3));
		Polygon cross = gaService.createPolygon(crossShape, points);
		cross.setFilled(false);
		cross.setLineWidth(1);
		peService.setPropertyValue(crossShape, DELETABLE_PROPERTY, "true");
		return cross;
	}

	public static Asterisk createGatewayAsterisk(ContainerShape container) {
		IPeService service = Graphiti.getPeService();

		Shape verticalShape = service.createShape(container, false);
		Polyline vertical = gaService.createPolyline(verticalShape, new int[] { 23, 8, 23, 42 });
		vertical.setLineWidth(5);
		peService.setPropertyValue(verticalShape, DELETABLE_PROPERTY, "true");

		Shape horizontalShape = service.createShape(container, false);
		Polyline horizontal = gaService.createPolyline(horizontalShape, new int[] { 8, 24, 42, 24 });
		horizontal.setLineWidth(5);
		peService.setPropertyValue(horizontalShape, DELETABLE_PROPERTY, "true");

		Shape diagonalDescShape = service.createShape(container, false);
		Polyline diagonalDesc = gaService.createPolyline(diagonalDescShape, new int[] { 13, 14, 37, 37 });
		diagonalDesc.setLineWidth(4);
		peService.setPropertyValue(diagonalDescShape, DELETABLE_PROPERTY, "true");

		Shape diagonalAscShape = service.createShape(container, false);
		Polyline diagonalAsc = gaService.createPolyline(diagonalAscShape, new int[] { 37, 14, 13, 37 });
		diagonalAsc.setLineWidth(4);
		peService.setPropertyValue(diagonalAscShape, DELETABLE_PROPERTY, "true");

		Asterisk a = new Asterisk();
		a.horizontal = horizontal;
		a.vertical = vertical;
		a.diagonalDesc = diagonalDesc;
		a.diagonalAsc = diagonalAsc;
		return a;
	}

	public static void clearGateway(PictogramElement element) {
		Iterator<PictogramElement> iterator = peService.getAllContainedPictogramElements(element).iterator();
		while (iterator.hasNext()) {
			PictogramElement childElement = iterator.next();
			boolean deletable = Boolean.parseBoolean(peService.getPropertyValue(childElement, DELETABLE_PROPERTY));
			if (deletable) {
				peService.deletePictogramElement(childElement);
			}
		}
	}

	/* EVENT */

	public static final int EVENT_SIZE = 36;
	public static final int EVENT_TEXT_AREA = 15;

	public static Ellipse createEventShape(Shape container) {
		Ellipse ellipse = gaService.createEllipse(container);
		gaService.setLocationAndSize(ellipse, 0, 0, EVENT_SIZE, EVENT_SIZE);
		return ellipse;
	}

	public static Envelope createEventEnvelope(Shape shape) {
		return createEnvelope(shape, 9, 9, 18, 18);
	}

	public static Polygon createEventPentagon(Shape shape) {
		int r = EVENT_SIZE / 2;
		return gaService.createPolygon(shape, new int[] { r, 7, r + 10, r - 4, r + 7, r + 10, r - 7, r + 10, r - 10,
		        r - 4 });
	}

	public static Ellipse createIntermediateEventCircle(Ellipse ellipse) {
		Ellipse circle = gaService.createEllipse(ellipse);
		gaService.setLocationAndSize(circle, 4, 4, EVENT_SIZE - 8, EVENT_SIZE - 8);
		circle.setLineWidth(1);
		circle.setFilled(false);
		return circle;
	}

	public static Image createEventImage(Shape shape, String imageId) {
		Image image = gaService.createImage(shape, imageId);
		gaService.setLocationAndSize(image, 8, 8, 20, 20);
		return image;
	}

	public static Polygon createEventSignal(Shape shape) {
		Polygon polygon = gaService.createPolygon(shape, new int[] { 16, 4, 28, 26, 7, 26 });
		polygon.setLineWidth(1);
		return polygon;
	}

	public static Polygon createEventEscalation(Shape shape) {
		int r = EVENT_SIZE / 2;
		int[] points = { r, 8, r + 8, r + 9, r, r + 2, r - 8, r + 9 };
		Polygon polygon = gaService.createPolygon(shape, points);
		polygon.setLineWidth(1);
		return polygon;
	}

	public static Compensation createEventCompensation(Shape shape) {
		Rectangle rect = gaService.createInvisibleRectangle(shape);

		int w = 22;
		int h = 18;
		gaService.setLocationAndSize(rect, 5, 9, w, h);

		int _w = w / 2;
		int _h = h / 2;
		int[] pontsArrow1 = { _w, 0, _w, h, 0, _h };
		Polygon arrow1 = gaService.createPolygon(rect, pontsArrow1);

		int[] pontsArrow2 = { w, 0, w, h, w / 2, _h };
		Polygon arrow2 = gaService.createPolygon(rect, pontsArrow2);

		Compensation compensation = new Compensation();
		compensation.arrow1 = arrow1;
		compensation.arrow2 = arrow2;
		return compensation;
	}

	public static Polygon createEventLink(Shape shape) {
		int r = EVENT_SIZE / 2;
		int[] points = { 32, r, 23, r + 11, 23, r + 6, 5, r + 6, 5, r - 6, 23, r - 6, 23, r - 11 };
		Polygon polygon = gaService.createPolygon(shape, points);
		polygon.setLineWidth(1);
		return polygon;
	}

	public static Polygon createEventError(Shape shape) {
		int r = EVENT_SIZE / 2;
		int[] points = { r + 4, r, r + 10, r - 10, r + 7, r + 10, r - 4, r, r - 10, r + 10, r - 7, r - 10 };
		Polygon polygon = gaService.createPolygon(shape, points);
		polygon.setLineWidth(1);
		return polygon;
	}

	public static Polygon createEventCancel(Shape shape) {
		int r = EVENT_SIZE / 2;
		int a = 9;
		int b = 12;
		int c = 4;
		int[] points = { r, r - c, r + a, r - b, r + b, r - a, r + c, r, r + b, r + a, r + a, r + b, r, r + c, r - a,
		        r + b, r - b, r + a, r - c, r, r - b, r - a, r - a, r - b };
		Polygon polygon = gaService.createPolygon(shape, points);
		polygon.setLineWidth(1);
		return polygon;
	}

	public static Ellipse createEventTerminate(Shape terminateShape) {
		Ellipse ellipse = gaService.createEllipse(terminateShape);
		gaService.setLocationAndSize(ellipse, 6, 6, EVENT_SIZE - 12, EVENT_SIZE - 12);
		ellipse.setLineWidth(1);
		ellipse.setFilled(true);
		return ellipse;
	}

	public static Polygon createEventParallelMultiple(Shape shape) {
		int r = EVENT_SIZE / 2;
		int a = 3;
		int b = 11;
		int[] points = { r - a, r - b, r + a, r - b, r + a, r - a, r + b, r - a, r + b, r + a, r + a, r + a, r + a,
		        r + b, r - a, r + b, r - a, r + a, r - b, r + a, r - b, r - a, r - a, r - a };
		Polygon cross = gaService.createPolygon(shape, points);
		cross.setFilled(false);
		cross.setLineWidth(1);
		return cross;
	}

	public static boolean clearEvent(ContainerShape shape) {
		boolean cleared = false;

		Iterator<PictogramElement> iterator = peService.getAllContainedPictogramElements(shape).iterator();
		while (iterator.hasNext()) {
			PictogramElement element = iterator.next();

			if (element.getLink() == null) {
				continue;
			}

			EList<EObject> objects = element.getLink().getBusinessObjects();

			if (!objects.isEmpty() && objects.size() > 1) {
				return false;
			}

			if (objects.get(0) != null && objects.get(0) instanceof EventDefinition) {
				peService.deletePictogramElement(element);
				cleared = true;
			}
		}

		return cleared;
	}

	/* OTHER */

	public static Envelope createEnvelope(GraphicsAlgorithmContainer gaContainer, int x, int y, int w, int h) {
		Rectangle rect = gaService.createRectangle(gaContainer);
		gaService.setLocationAndSize(rect, x, y, w, h);
		rect.setFilled(false);

		Polyline line = gaService.createPolyline(rect, new int[] { 0, 0, w / 2, h / 2, w, 0 });

		Envelope envelope = new Envelope();
		envelope.rect = rect;
		envelope.line = line;

		return envelope;
	}

	public static Polygon createDataArrow(Polygon p) {
		int[] points = { 4, 8, 14, 8, 14, 4, 18, 10, 14, 16, 14, 12, 4, 12 };
		Polygon arrow = gaService.createPolygon(p, points);
		arrow.setLineWidth(1);
		return arrow;
	}

	// ACTIVITY

	public static final int TASK_DEFAULT_WIDTH = 110;
	public static final int TASK_DEFAULT_HEIGHT = 50;
	public static final int TASK_IMAGE_SIZE = 16;

	public static final int SUB_PROCEESS_DEFAULT_WIDTH = 300;
	public static final int SUB_PROCESS_DEFAULT_HEIGHT = 300;

	public static final int MARKER_WIDTH = 10;
	public static final int MARKER_HEIGHT = 10;

	public static final String ACTIVITY_MARKER_CONTAINER = "activity.marker.container";
	public static final String ACTIVITY_MARKER_COMPENSATE = "activity.marker.compensate";
	public static final String ACTIVITY_MARKER_LOOP_CHARACTERISTIC = "activity.marker.loop.characteristic";
	public static final String ACTIVITY_MARKER_AD_HOC = "activity.marker.adhoc";
	public static final String ACTIVITY_MARKER_EXPAND = "activity.marker.expand";

	public static Compensation createActivityMarkerCompensate(ContainerShape markerContainer) {
		GraphicsAlgorithmContainer algorithmContainer = createActivityMarkerGaContainer(markerContainer,
		        ACTIVITY_MARKER_COMPENSATE);
		return createCompensation(algorithmContainer, MARKER_WIDTH, MARKER_HEIGHT);
	}

	public static Loop createActivityMarkerStandardLoop(ContainerShape markerContainer) {
		GraphicsAlgorithmContainer algorithmContainer = createActivityMarkerGaContainer(markerContainer,
		        ACTIVITY_MARKER_LOOP_CHARACTERISTIC);

		int[] xy = { 8, 10, 10, 5, 5, 0, 0, 5, 3, 10 };
		int[] bend = { 0, 0, 3, 4, 4, 4, 4, 3, 3, 0 };
		Polyline circle = gaService.createPolyline(algorithmContainer, xy, bend);

		Loop loop = new Loop();
		loop.circle = circle;
		loop.arrow = gaService.createPolyline(algorithmContainer, new int[] { 5, 5, 5, 10, 0, 10 });
		return loop;
	}

	public static MultiInstance createActivityMarkerMultiParallel(ContainerShape markerContainer) {
		GraphicsAlgorithmContainer algorithmContainer = createActivityMarkerGaContainer(markerContainer,
		        ACTIVITY_MARKER_LOOP_CHARACTERISTIC);
		MultiInstance multiInstance = new MultiInstance();
		multiInstance.line1 = gaService.createPolyline(algorithmContainer, new int[] { 2, 0, 2, MARKER_HEIGHT });
		multiInstance.line2 = gaService.createPolyline(algorithmContainer, new int[] { 5, 0, 5, MARKER_HEIGHT });
		multiInstance.line3 = gaService.createPolyline(algorithmContainer, new int[] { 8, 0, 8, MARKER_HEIGHT });
		return multiInstance;
	}

	public static MultiInstance createActivityMarkerMultiSequential(ContainerShape markerContainer) {
		GraphicsAlgorithmContainer algorithmContainer = createActivityMarkerGaContainer(markerContainer,
		        ACTIVITY_MARKER_LOOP_CHARACTERISTIC);
		MultiInstance multiInstance = new MultiInstance();
		multiInstance.line1 = gaService.createPolyline(algorithmContainer, new int[] { 0, 2, MARKER_WIDTH, 2 });
		multiInstance.line2 = gaService.createPolyline(algorithmContainer, new int[] { 0, 5, MARKER_WIDTH, 5 });
		multiInstance.line3 = gaService.createPolyline(algorithmContainer, new int[] { 0, 8, MARKER_WIDTH, 8 });
		return multiInstance;
	}

	public static Polyline createActivityMarkerAdHoc(ContainerShape markerContainer) {
		GraphicsAlgorithmContainer algorithmContainer = createActivityMarkerGaContainer(markerContainer,
		        ACTIVITY_MARKER_AD_HOC);
		int[] xy = { 0, 8, 3, 2, 7, 8, 10, 2 };
		int[] bend = { 0, 3, 3, 3, 3, 3, 3, 0 };
		return gaService.createPolyline(algorithmContainer, xy, bend);
	}

	public static Expand createActivityMarkerExpand(ContainerShape markerContainer) {
		GraphicsAlgorithmContainer algorithmContainer = createActivityMarkerGaContainer(markerContainer,
		        ACTIVITY_MARKER_EXPAND);

		Rectangle rect = gaService.createRectangle(algorithmContainer);
		rect.setFilled(false);
		gaService.setLocationAndSize(rect, 0, 0, 10, 10);

		Expand expand = new Expand();
		expand.rect = rect;
		expand.horizontal = gaService.createPolyline(algorithmContainer, new int[] { 0, 5, 10, 5 });
		expand.vertical = gaService.createPolyline(algorithmContainer, new int[] { 5, 0, 5, 10 });
		return expand;
	}

	public static void clearActivityMarker(ContainerShape markerContainer, String property) {

		int totalWidth = 0;
		int parentW = ((ContainerShape) markerContainer.eContainer()).getGraphicsAlgorithm().getWidth();
		int lastX = -1;

		Iterator<Shape> iterator = peService.getAllContainedShapes(markerContainer).iterator();
		while (iterator.hasNext()) {
			Shape shape = iterator.next();
			String value = peService.getPropertyValue(shape, property);
			GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
			if (value != null && new Boolean(value)) {
				lastX = ga.getX();
				peService.deletePictogramElement(shape);
			} else {
				totalWidth += ga.getWidth();
				if (lastX != -1) {
					gaService.setLocation(ga, lastX, ga.getY(), true);
					lastX = ga.getX() + ga.getWidth();
				}
			}
		}

		totalWidth = totalWidth == 0 ? 10 : totalWidth;
		GraphicsAlgorithm ga = markerContainer.getGraphicsAlgorithm();
		gaService.setLocationAndSize(ga, (parentW / 2) - (totalWidth / 2), ga.getY(), totalWidth, MARKER_HEIGHT);
	}

	private static GraphicsAlgorithmContainer createActivityMarkerGaContainer(ContainerShape markerContainer,
	        String property) {
		GraphicsAlgorithm ga = markerContainer.getGraphicsAlgorithm();

		int totalWidth = MARKER_WIDTH;
		int parentW = ((ContainerShape) markerContainer.eContainer()).getGraphicsAlgorithm().getWidth();
		int lastX = 0;

		Iterator<Shape> iterator = peService.getAllContainedShapes(markerContainer).iterator();
		while (iterator.hasNext()) {
			Shape containedShape = (Shape) iterator.next();
			GraphicsAlgorithm containedGa = containedShape.getGraphicsAlgorithm();
			totalWidth += containedGa.getWidth();
			lastX = containedGa.getX() + containedGa.getWidth();
		}

		gaService.setLocationAndSize(ga, (parentW / 2) - (totalWidth / 2), ga.getY(), totalWidth, MARKER_HEIGHT);

		Shape shape = peService.createShape(markerContainer, false);
		peService.setPropertyValue(shape, property, Boolean.toString(true));
		Rectangle invisibleRect = gaService.createInvisibleRectangle(shape);
		gaService.setLocationAndSize(invisibleRect, lastX, 0, MARKER_WIDTH, MARKER_HEIGHT);

		return invisibleRect;
	}

	private static Compensation createCompensation(GraphicsAlgorithmContainer container, int w, int h) {
		int[] xy = { 0, h / 2, w / 2, 0, w / 2, h };
		Polygon arrow1 = gaService.createPolygon(container, xy);
		arrow1.setFilled(false);

		xy = new int[] { w / 2, h / 2, w, 0, w, h };
		Polygon arrow2 = gaService.createPolygon(container, xy);
		arrow2.setFilled(false);

		Compensation compensation = new Compensation();
		compensation.arrow1 = arrow1;
		compensation.arrow2 = arrow2;

		return compensation;
	}
}