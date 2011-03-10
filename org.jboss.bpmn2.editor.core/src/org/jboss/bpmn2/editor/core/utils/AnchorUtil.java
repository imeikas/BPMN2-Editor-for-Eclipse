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
			Anchor anchor = (Anchor) iterator.next();
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

	public static Tuple<FixPointAnchor, FixPointAnchor> getSourceAndTargetBoundaryAnchors(Shape source, Shape target) {
		Map<AnchorLocation, BoundaryAnchor> sourceBoundaryAnchors = getBoundaryAnchors(source);
		Map<AnchorLocation, BoundaryAnchor> targetBoundaryAnchors = getBoundaryAnchors(target);

		// if source top is lower than target bottom then use source top and target bottom
		BoundaryAnchor sourceTop = sourceBoundaryAnchors.get(AnchorLocation.TOP);
		BoundaryAnchor targetBottom = targetBoundaryAnchors.get(AnchorLocation.BOTTOM);
		if (sourceTop.location.getY() > targetBottom.location.getY()) {
			return new Tuple<FixPointAnchor, FixPointAnchor>(sourceTop.anchor, targetBottom.anchor);
		}

		// if source bottom is higher than target top then use source bottom and target top
		BoundaryAnchor sourceBottom = sourceBoundaryAnchors.get(AnchorLocation.BOTTOM);
		BoundaryAnchor targetTop = targetBoundaryAnchors.get(AnchorLocation.TOP);
		if (sourceBottom.location.getY() < targetTop.location.getY()) {
			return new Tuple<FixPointAnchor, FixPointAnchor>(sourceBottom.anchor, targetTop.anchor);
		}

		// if source left is further than target right then use source left and target right
		BoundaryAnchor sourceLeft = sourceBoundaryAnchors.get(AnchorLocation.LEFT);
		BoundaryAnchor targetRight = targetBoundaryAnchors.get(AnchorLocation.RIGHT);
		if (sourceLeft.location.getX() > targetRight.location.getX()) {
			return new Tuple<FixPointAnchor, FixPointAnchor>(sourceLeft.anchor, targetRight.anchor);
		}

		// if source right is smaller than target left then use source right and target left
		BoundaryAnchor sourceRight = sourceBoundaryAnchors.get(AnchorLocation.RIGHT);
		BoundaryAnchor targetLeft = targetBoundaryAnchors.get(AnchorLocation.LEFT);
		if (sourceRight.location.getX() < targetLeft.location.getX()) {
			return new Tuple<FixPointAnchor, FixPointAnchor>(sourceRight.anchor, targetLeft.anchor);
		}

		return new Tuple<FixPointAnchor, FixPointAnchor>(sourceTop.anchor, targetTop.anchor);
	}

	public static void reConnect(BPMNShape shape, Diagram diagram) {
		try {
			ModelHandler handler = FeatureSupport.getModelHanderInstance(diagram);
			for (BPMNEdge bpmnEdge : handler.getAll(BPMNEdge.class)) {
				if (bpmnEdge.getSourceElement().getId().equals(shape.getId())
				        || bpmnEdge.getTargetElement().getId().equals(shape.getId())) {
					updateEdge(bpmnEdge, diagram);
				}
			}
		} catch (Exception e) {
			Activator.logError(e);
		}
	}

	private static void updateEdge(BPMNEdge edge, Diagram diagram) {
		ContainerShape source = (ContainerShape) Graphiti.getLinkService()
		        .getPictogramElements(diagram, (BPMNShape) edge.getSourceElement()).get(0);
		ContainerShape target = (ContainerShape) Graphiti.getLinkService()
		        .getPictogramElements(diagram, (BPMNShape) edge.getTargetElement()).get(0);
		Tuple<FixPointAnchor, FixPointAnchor> anchors = getSourceAndTargetBoundaryAnchors(source, target);

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