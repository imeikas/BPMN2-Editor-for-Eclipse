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

	/* GATEWAY */

	public static final int GATEWAY_RADIUS = 25;
	private static final int[] GATEWAY = { 0, GATEWAY_RADIUS, GATEWAY_RADIUS, 0, 2 * GATEWAY_RADIUS, GATEWAY_RADIUS,
	        GATEWAY_RADIUS, 2 * GATEWAY_RADIUS };

	public static Polygon createGateway(Shape container) {
		return gaService.createPolygon(container, GATEWAY);
	}

	public static Polygon createPentagon(Polygon gateway) {
		return gaService.createPolygon(gateway, new int[] { GATEWAY_RADIUS, 18, GATEWAY_RADIUS + 8, GATEWAY_RADIUS - 2,
		        GATEWAY_RADIUS + 5, GATEWAY_RADIUS + 7, GATEWAY_RADIUS - 5, GATEWAY_RADIUS + 7, GATEWAY_RADIUS - 8,
		        GATEWAY_RADIUS - 2 });
	}

	public static Ellipse createInnerCircle(Polygon gateway) {
		Ellipse ellipse = gaService.createEllipse(gateway);
		gaService.setLocationAndSize(ellipse, 14, 14, 23, 23);
		return ellipse;
	}

	public static Ellipse createOuterCircle(Polygon gateway) {
		Ellipse ellipse = gaService.createEllipse(gateway);
		gaService.setLocationAndSize(ellipse, 12, 12, 27, 27);
		ellipse.setFilled(false);
		return ellipse;
	}

	public static Polygon createCross(Polygon gateway) {
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
		Polygon cross = gaService.createPolygon(gateway, points);
		cross.setFilled(false);
		cross.setLineWidth(1);
		return cross;
	}

	public static Asterisk createAsterisk(Polygon gateway) {
		Polyline vertical = gaService.createPolyline(gateway, new int[] { 24, 8, 24, 42 });
		vertical.setLineWidth(5);

		Polyline horizontal = gaService.createPolyline(gateway, new int[] { 8, 24, 42, 24 });
		horizontal.setLineWidth(5);

		Polyline diagonalDesc = gaService.createPolyline(gateway, new int[] { 13, 14, 37, 37 });
		diagonalDesc.setLineWidth(4);

		Polyline diagonalAsc = gaService.createPolyline(gateway, new int[] { 37, 13, 14, 37 });
		diagonalAsc.setLineWidth(4);

		Asterisk a = new Asterisk();
		a.horizontal = horizontal;
		a.vertical = vertical;
		a.diagonalDesc = diagonalDesc;
		a.diagonalAsc = diagonalAsc;
		return a;
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
		int radius = EVENT_SIZE / 2;
		return gaService.createPolygon(shape, new int[] { radius, 7, radius + 10, radius - 4, radius + 7, radius + 10,
		        radius - 7, radius + 10, radius - 10, radius - 4 });
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
}
