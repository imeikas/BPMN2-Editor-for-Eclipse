package org.jboss.bpmn2.editor.core.features;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
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

public class ShapeUtil {

	private static final IGaService gaService = Graphiti.getGaService();
	private static final IPeService peService = Graphiti.getPeService();

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

	/* GATEWAY */

	private static final String DELETABLE_PROPERTY = "deletable";

	public static final int GATEWAY_RADIUS = 25;

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
			PictogramElement childElement = (PictogramElement) iterator.next();
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
			PictogramElement element = (PictogramElement) iterator.next();

			if (element.getLink() == null)
				continue;

			EList<EObject> objects = element.getLink().getBusinessObjects();

			if (!objects.isEmpty() && objects.size() > 1) // already a multiple event
				return false;

			if (objects.get(0) != null && objects.get(0) instanceof EventDefinition) {
				peService.deletePictogramElement(element);
				cleared = true;
			}
		}

		return cleared;
	}

	/* OTHER */

	private static Envelope createEnvelope(Shape shape, int x, int y, int w, int h) {
		Rectangle rect = gaService.createRectangle(shape);
		gaService.setLocationAndSize(rect, x, y, w, h);
		Polyline line = gaService.createPolyline(rect, new int[] { 0, 0, w / 2, h / 2, w, 0 });

		Envelope envelope = new Envelope();
		envelope.rect = rect;
		envelope.line = line;

		return envelope;
	}

	public static Polygon createDataArrow(Polygon p) {
		int[] points = {4, 8, 
						14, 8, 
						14, 4, 
						18, 10, 
						14, 16,
						14, 12,
						4, 12};
		Polygon arrow = gaService.createPolygon(p, points);
		arrow.setLineWidth(1);
	    return arrow;
    }
}
