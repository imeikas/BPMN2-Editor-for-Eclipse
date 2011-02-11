package org.jboss.bpmn2.editor.core.features;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

public class ShapeUtil {

	private static final IGaService gaService = Graphiti.getGaService();

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
		Polyline vertical = gaService.createPolyline(gateway, new int [] {24, 8, 24, 42});
		vertical.setLineWidth(5);
		
		Polyline horizontal = gaService.createPolyline(gateway, new int [] {8, 24, 42, 24});
		horizontal.setLineWidth(5);
		
		Polyline diagonalDesc = gaService.createPolyline(gateway, new int [] {13, 14, 37, 37});
		diagonalDesc.setLineWidth(4);
		
		Polyline diagonalAsc = gaService.createPolyline(gateway, new int [] {37, 13, 14, 37});
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
		gaService.setSize(ellipse, EVENT_SIZE, EVENT_SIZE);
		return ellipse;
	}

	public static Envelope createEnvelope(Ellipse event) {
		return createEnvelope(event, 9, 9, 18, 18);
	}
	
	public static Ellipse createTriggerCircle(Ellipse event) {
		Ellipse circle = gaService.createEllipse(event);
		gaService.setLocationAndSize(circle, 4, 4, EVENT_SIZE - 8, EVENT_SIZE - 8);
		circle.setLineWidth(1);
		circle.setFilled(false);
		return circle;
	}
	
	/* OTHER */
	
	public static Envelope createEnvelope(Shape container) {
		return createEnvelope(container, 0, 0, 18, 18);
	}
	
	private static Envelope createEnvelope(GraphicsAlgorithmContainer container, int x, int y, int w, int h) {
		Rectangle rect = gaService.createRectangle(container);
		gaService.setLocationAndSize(rect, x, y, w, h);
		
		Polyline line = gaService.createPolyline(rect, new int[] {0, 0, x / 2, y / 2, 2 * w, 0});
		
		Envelope envelope = new Envelope();
		envelope.rect = rect;
		envelope.line = line;
		
		return envelope;
	}
}
