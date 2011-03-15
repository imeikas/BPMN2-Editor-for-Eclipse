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
package org.jboss.bpmn2.editor.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.mm.pictograms.impl.FreeFormConnectionImpl;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class AnchorUtil {

	public static final String BOUNDARY_FIXPOINT_ANCHOR = "boundary.fixpoint.anchor";

	private static final IPeService peService = Graphiti.getPeService();
	private static final IGaService gaService = Graphiti.getGaService();

	public enum AnchorLocation {
		TOP("anchor.top"), BOTTOM("anchor.bottom"), LEFT("anchor.left"), RIGHT("anchor.right");

		private final String key;

		private AnchorLocation(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}

		public static AnchorLocation getLocation(String key) {
			for (AnchorLocation l : values()) {
				if (l.getKey().equals(key)) {
					return l;
				}
			}
			return null;
		}
	}

	public static class AnchorTuple {
		public FixPointAnchor sourceAnchor;
		public FixPointAnchor targetAnchor;
	}

	public static class BoundaryAnchor {
		public FixPointAnchor anchor;
		public AnchorLocation locationType;
		public ILocation location;
	}

	public static FixPointAnchor createAnchor(Shape s, AnchorLocation loc, int x, int y) {
		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();

		FixPointAnchor anchor = peService.createFixPointAnchor(s);
		peService.setPropertyValue(anchor, BOUNDARY_FIXPOINT_ANCHOR, loc.getKey());
		anchor.setLocation(gaService.createPoint(x, y));
		gaService.createInvisibleRectangle(anchor);

		return anchor;
	}

	public static Map<AnchorLocation, BoundaryAnchor> getBoundaryAnchors(Shape s) {
		Map<AnchorLocation, BoundaryAnchor> map = new HashMap<AnchorLocation, AnchorUtil.BoundaryAnchor>(4);
		Iterator<Anchor> iterator = s.getAnchors().iterator();
		while (iterator.hasNext()) {
			Anchor anchor = iterator.next();
			String property = Graphiti.getPeService().getPropertyValue(anchor, BOUNDARY_FIXPOINT_ANCHOR);
			if (property != null && anchor instanceof FixPointAnchor) {
				BoundaryAnchor a = new BoundaryAnchor();
				a.anchor = (FixPointAnchor) anchor;
				a.locationType = AnchorLocation.getLocation(property);
				a.location = peService.getLocationRelativeToDiagram(anchor);
				map.put(a.locationType, a);
			}
		}
		return map;
	}

	public static Point getCenterPoint(Shape s) {
		GraphicsAlgorithm ga = s.getGraphicsAlgorithm();
		ILocation loc = peService.getLocationRelativeToDiagram(s);
		return gaService.createPoint(loc.getX() + (ga.getWidth() / 2), loc.getY() + (ga.getHeight() / 2));
	}

	@SuppressWarnings("restriction")
	public static Tuple<FixPointAnchor, FixPointAnchor> getSourceAndTargetBoundaryAnchors(Shape source, Shape target,
			Connection connection) {
		Map<AnchorLocation, BoundaryAnchor> sourceBoundaryAnchors = getBoundaryAnchors(source);
		Map<AnchorLocation, BoundaryAnchor> targetBoundaryAnchors = getBoundaryAnchors(target);

		if (connection instanceof FreeFormConnectionImpl) {
			EList<Point> bendpoints = ((FreeFormConnectionImpl) connection).getBendpoints();
			if (bendpoints.size() > 0) {
				FixPointAnchor sourceAnchor = getCorrectAnchor(sourceBoundaryAnchors, bendpoints.get(0));
				FixPointAnchor targetAnchor = getCorrectAnchor(targetBoundaryAnchors,
						bendpoints.get(bendpoints.size() - 1));
				return new Tuple<FixPointAnchor, FixPointAnchor>(sourceAnchor, targetAnchor);
			}
		}

		BoundaryAnchor sourceTop = sourceBoundaryAnchors.get(AnchorLocation.TOP);
		BoundaryAnchor sourceBottom = sourceBoundaryAnchors.get(AnchorLocation.BOTTOM);
		BoundaryAnchor sourceLeft = sourceBoundaryAnchors.get(AnchorLocation.LEFT);
		BoundaryAnchor sourceRight = sourceBoundaryAnchors.get(AnchorLocation.RIGHT);
		BoundaryAnchor targetBottom = targetBoundaryAnchors.get(AnchorLocation.BOTTOM);
		BoundaryAnchor targetTop = targetBoundaryAnchors.get(AnchorLocation.TOP);
		BoundaryAnchor targetRight = targetBoundaryAnchors.get(AnchorLocation.RIGHT);
		BoundaryAnchor targetLeft = targetBoundaryAnchors.get(AnchorLocation.LEFT);

		boolean sLower = sourceTop.location.getY() > targetBottom.location.getY();
		boolean sHigher = sourceBottom.location.getY() < targetTop.location.getY();
		boolean sRight = sourceLeft.location.getX() > targetRight.location.getX();
		boolean sLeft = sourceRight.location.getX() < targetLeft.location.getX();

		if (sLower) {
			if (!sLeft && !sRight) {
				return new Tuple<FixPointAnchor, FixPointAnchor>(sourceTop.anchor, targetBottom.anchor);
			} else if (sLeft) {
				FixPointAnchor fromTopAnchor = getCorrectAnchor(targetBoundaryAnchors,
						peService.getLocationRelativeToDiagram(sourceTop.anchor));
				FixPointAnchor fromRightAnchor = getCorrectAnchor(targetBoundaryAnchors,
						peService.getLocationRelativeToDiagram(sourceRight.anchor));

				double topLength = getLength(peService.getLocationRelativeToDiagram(fromTopAnchor),
						peService.getLocationRelativeToDiagram(sourceTop.anchor));
				double rightLength = getLength(peService.getLocationRelativeToDiagram(fromRightAnchor),
						peService.getLocationRelativeToDiagram(sourceRight.anchor));

				if (topLength < rightLength) {
					return new Tuple<FixPointAnchor, FixPointAnchor>(sourceBottom.anchor, fromTopAnchor);
				} else {
					return new Tuple<FixPointAnchor, FixPointAnchor>(sourceRight.anchor, fromRightAnchor);
				}
			} else {
				FixPointAnchor fromTopAnchor = getCorrectAnchor(targetBoundaryAnchors,
						peService.getLocationRelativeToDiagram(sourceTop.anchor));
				FixPointAnchor fromLeftAnchor = getCorrectAnchor(targetBoundaryAnchors,
						peService.getLocationRelativeToDiagram(sourceLeft.anchor));

				double topLength = getLength(peService.getLocationRelativeToDiagram(fromTopAnchor),
						peService.getLocationRelativeToDiagram(sourceTop.anchor));
				double leftLength = getLength(peService.getLocationRelativeToDiagram(fromLeftAnchor),
						peService.getLocationRelativeToDiagram(sourceLeft.anchor));
				if (topLength < leftLength) {
					return new Tuple<FixPointAnchor, FixPointAnchor>(sourceTop.anchor, fromTopAnchor);
				} else {
					return new Tuple<FixPointAnchor, FixPointAnchor>(sourceLeft.anchor, fromLeftAnchor);
				}
			}

		}

		if (sHigher) {
			if (!sLeft && !sRight) {
				return new Tuple<FixPointAnchor, FixPointAnchor>(sourceBottom.anchor, targetTop.anchor);
			} else if (sLeft) {
				FixPointAnchor fromBottomAnchor = getCorrectAnchor(targetBoundaryAnchors,
						peService.getLocationRelativeToDiagram(sourceBottom.anchor));
				FixPointAnchor fromRightAnchor = getCorrectAnchor(targetBoundaryAnchors,
						peService.getLocationRelativeToDiagram(sourceRight.anchor));

				double bottomLength = getLength(peService.getLocationRelativeToDiagram(fromBottomAnchor),
						peService.getLocationRelativeToDiagram(sourceBottom.anchor));
				double rightLength = getLength(peService.getLocationRelativeToDiagram(fromRightAnchor),
						peService.getLocationRelativeToDiagram(sourceRight.anchor));
				System.out.println(bottomLength + " " + rightLength);

				if (bottomLength < rightLength) {
					return new Tuple<FixPointAnchor, FixPointAnchor>(sourceBottom.anchor, fromBottomAnchor);
				} else {
					return new Tuple<FixPointAnchor, FixPointAnchor>(sourceRight.anchor, fromRightAnchor);
				}
			} else {
				FixPointAnchor fromBottomAnchor = getCorrectAnchor(targetBoundaryAnchors,
						peService.getLocationRelativeToDiagram(sourceBottom.anchor));
				FixPointAnchor fromLeftAnchor = getCorrectAnchor(targetBoundaryAnchors,
						peService.getLocationRelativeToDiagram(sourceLeft.anchor));

				double bottomLength = getLength(peService.getLocationRelativeToDiagram(fromBottomAnchor),
						peService.getLocationRelativeToDiagram(sourceBottom.anchor));
				double leftLength = getLength(peService.getLocationRelativeToDiagram(fromLeftAnchor),
						peService.getLocationRelativeToDiagram(sourceLeft.anchor));
				if (bottomLength < leftLength) {
					return new Tuple<FixPointAnchor, FixPointAnchor>(sourceBottom.anchor, fromBottomAnchor);
				} else {
					return new Tuple<FixPointAnchor, FixPointAnchor>(sourceLeft.anchor, fromLeftAnchor);
				}
			}
		}

		// if source left is further than target right then use source left and target right
		if (sRight) {
			return new Tuple<FixPointAnchor, FixPointAnchor>(sourceLeft.anchor, targetRight.anchor);
		}

		// if source right is smaller than target left then use source right and target left
		if (sLeft) {
			return new Tuple<FixPointAnchor, FixPointAnchor>(sourceRight.anchor, targetLeft.anchor);
		}

		return new Tuple<FixPointAnchor, FixPointAnchor>(sourceTop.anchor, targetTop.anchor);
	}

	private static FixPointAnchor getCorrectAnchor(Map<AnchorLocation, BoundaryAnchor> targetBoundaryAnchors,
			ILocation loc) {
		return getCorrectAnchor(targetBoundaryAnchors, gaService.createPoint(loc.getX(), loc.getY()));
	}

	private static double getLength(ILocation start, ILocation end) {
		return Math.sqrt(Math.pow(start.getX() - end.getX(), 2) + Math.pow(start.getY() - end.getY(), 2));
	}

	private static FixPointAnchor getCorrectAnchor(Map<AnchorLocation, BoundaryAnchor> boundaryAnchors, Point point) {

		BoundaryAnchor bottom = boundaryAnchors.get(AnchorLocation.BOTTOM);
		BoundaryAnchor top = boundaryAnchors.get(AnchorLocation.TOP);
		BoundaryAnchor right = boundaryAnchors.get(AnchorLocation.RIGHT);
		BoundaryAnchor left = boundaryAnchors.get(AnchorLocation.LEFT);

		boolean pointLower = point.getY() > bottom.location.getY();
		boolean pointHigher = point.getY() < top.location.getY();
		boolean pointRight = point.getX() > right.location.getX();
		boolean pointLeft = point.getX() < left.location.getX();

		// Find the best connector.
		if (pointLower) {
			if (!pointLeft && !pointRight) {
				// bendpoint is straight below the shape
				return bottom.anchor;
			} else if (pointLeft) {

				int deltaX = left.location.getX() - point.getX();
				int deltaY = point.getY() - bottom.location.getY();
				if (deltaX > deltaY) {
					return left.anchor;
				} else {
					return bottom.anchor;
				}
			} else {
				int deltaX = point.getX() - right.location.getX();
				int deltaY = point.getY() - bottom.location.getY();
				if (deltaX > deltaY) {
					return right.anchor;
				} else {
					return bottom.anchor;
				}
			}
		}

		if (pointHigher) {
			if (!pointLeft && !pointRight) {
				// bendpoint is straight above the shape
				return top.anchor;
			} else if (pointLeft) {
				int deltaX = left.location.getX() - point.getX();
				int deltaY = top.location.getY() - point.getY();
				if (deltaX > deltaY) {
					return left.anchor;
				} else {
					return top.anchor;
				}
			} else {
				int deltaX = point.getX() - right.location.getX();
				int deltaY = top.location.getY() - point.getY();
				if (deltaX > deltaY) {
					return right.anchor;
				} else {
					return top.anchor;
				}
			}

		}

		// if we reach here, then the point is neither above or below the shape and we only need to determine if we need
		// to connect to the left or right part of the shape
		if (pointRight) {
			return right.anchor;
		}

		if (pointLeft) {
			return left.anchor;
		}

		return top.anchor;
	}

	public static void reConnect(BPMNShape shape, Diagram diagram) {
		try {
			ModelHandler handler = FeatureSupport.getModelHanderInstance(diagram);
			for (BPMNEdge bpmnEdge : handler.getAll(BPMNEdge.class)) {
				DiagramElement sourceElement = bpmnEdge.getSourceElement();
				DiagramElement targetElement = bpmnEdge.getTargetElement();
				if (sourceElement != null && targetElement != null) {
					boolean sourceMatches = sourceElement.getId().equals(shape.getId());
					boolean targetMatches = targetElement.getId().equals(shape.getId());
					if (sourceMatches || targetMatches) {
						updateEdge(bpmnEdge, diagram);
					}
				}
			}
		} catch (Exception e) {
			Activator.logError(e);
		}
	}

	private static void updateEdge(BPMNEdge edge, Diagram diagram) {
		ContainerShape source = (ContainerShape) Graphiti.getLinkService()
				.getPictogramElements(diagram, edge.getSourceElement()).get(0);
		ContainerShape target = (ContainerShape) Graphiti.getLinkService()
				.getPictogramElements(diagram, edge.getTargetElement()).get(0);
		Connection connection = (Connection) Graphiti.getLinkService().getPictogramElements(diagram, edge).get(0);
		Tuple<FixPointAnchor, FixPointAnchor> anchors = getSourceAndTargetBoundaryAnchors(source, target, connection);

		ILocation loc = peService.getLocationRelativeToDiagram(anchors.getFirst());
		org.eclipse.dd.dc.Point p = edge.getWaypoint().get(0);
		p.setX(loc.getX());
		p.setY(loc.getY());

		loc = peService.getLocationRelativeToDiagram(anchors.getSecond());
		p = edge.getWaypoint().get(edge.getWaypoint().size() - 1);
		p.setX(loc.getX());
		p.setY(loc.getY());

		relocateConnection(source.getAnchors(), anchors, target);
		deleteEmptyAdHocAnchors(source);
		deleteEmptyAdHocAnchors(target);
	}

	private static void relocateConnection(EList<Anchor> anchors, Tuple<FixPointAnchor, FixPointAnchor> newAnchors,
			ContainerShape target) {

		List<Connection> connectionsToBeUpdated = new ArrayList<Connection>();

		for (Anchor anchor : anchors) {
			if (!(anchor instanceof FixPointAnchor)) {
				continue;
			}

			for (Connection connection : anchor.getOutgoingConnections()) {
				if (connection.getEnd().eContainer().equals(target)) {
					connectionsToBeUpdated.add(connection);
				}
			}
		}

		for (Connection c : connectionsToBeUpdated) {
			c.setStart(newAnchors.getFirst());
			c.setEnd(newAnchors.getSecond());
		}
	}

	private static void deleteEmptyAdHocAnchors(Shape s) {
		List<Integer> indexes = new ArrayList<Integer>();

		for (int i = 0; i < s.getAnchors().size(); i++) {
			Anchor a = s.getAnchors().get(i);
			if (!(a instanceof FixPointAnchor)) {
				continue;
			}

			if (peService.getProperty(a, BOUNDARY_FIXPOINT_ANCHOR) == null && a.getIncomingConnections().isEmpty()
					&& a.getOutgoingConnections().isEmpty()) {
				indexes.add(i);
			}
		}

		for (int i : indexes) {
			peService.deletePictogramElement(s.getAnchors().get(i));
		}
	}

	public static void addFixedPointAnchors(Shape shape, GraphicsAlgorithm ga) {
		int w = ga.getWidth();
		int h = ga.getHeight();
		AnchorUtil.createAnchor(shape, AnchorLocation.TOP, w / 2, 0);
		AnchorUtil.createAnchor(shape, AnchorLocation.RIGHT, w, h / 2);
		AnchorUtil.createAnchor(shape, AnchorLocation.BOTTOM, w / 2, h);
		AnchorUtil.createAnchor(shape, AnchorLocation.LEFT, 0, h / 2);
	}
}