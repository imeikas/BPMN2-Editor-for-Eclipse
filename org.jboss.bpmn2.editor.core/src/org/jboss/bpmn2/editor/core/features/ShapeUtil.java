package org.jboss.bpmn2.editor.core.features;

import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

public class ShapeUtil {

	private static final IGaService gaService = Graphiti.getGaService();

	public static class Envelope {
		public Rectangle rect;
		public Polyline line;
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
		return ellipse;
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
