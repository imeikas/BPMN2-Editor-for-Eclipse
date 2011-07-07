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
package org.eclipse.bpmn2.modeler.core.di;

import java.io.IOException;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.dc.DcFactory;
import org.eclipse.dd.dc.Point;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.mm.pictograms.impl.FreeFormConnectionImpl;
import org.eclipse.graphiti.services.Graphiti;

public class DIUtils {

	public static void updateDIShape(PictogramElement element) {

		PictogramLink link = element.getLink();
		if (link == null) {
			return;
		}

		BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(element, BPMNShape.class);
		if (bpmnShape == null) {
			return;
		}

		ILocation loc = Graphiti.getLayoutService().getLocationRelativeToDiagram((Shape) element);
		Bounds bounds = bpmnShape.getBounds();

		bounds.setX(loc.getX());
		bounds.setY(loc.getY());

		GraphicsAlgorithm graphicsAlgorithm = element.getGraphicsAlgorithm();
		IDimension size = Graphiti.getGaService().calculateSize(graphicsAlgorithm);
		bounds.setHeight(size.getHeight());
		bounds.setWidth(size.getWidth());

		if (element instanceof ContainerShape) {
			EList<Shape> children = ((ContainerShape) element).getChildren();
			for (Shape shape : children) {
				if (shape instanceof ContainerShape) {
					updateDIShape(shape);
				}
			}
		}
	}

	public static void updateDIEdge(Diagram diagram, Connection connection, Class clazz) {
		try {
			ModelHandler modelHandler = ModelHandlerLocator.getModelHandler(connection.getLink().getBusinessObjects()
					.get(0).eResource());

			EObject be = BusinessObjectUtil.getFirstElementOfType(connection, clazz);
			BPMNEdge edge = (BPMNEdge) modelHandler.findDIElement(diagram, (BaseElement) be);
			Point point = DcFactory.eINSTANCE.createPoint();

			List<Point> waypoint = edge.getWaypoint();
			waypoint.clear();

			GraphicsAlgorithm graphicsAlgorithm = connection.getStart().getGraphicsAlgorithm();
			// FIXME connections must create anchors!!!
			if (graphicsAlgorithm != null) {
				point.setX(graphicsAlgorithm.getX());
				point.setY(graphicsAlgorithm.getY());
			} else {
				point.setX(connection.getStart().getParent().getGraphicsAlgorithm().getX());
				point.setY(connection.getStart().getParent().getGraphicsAlgorithm().getY());
			}
			waypoint.add(point);

			if (connection instanceof FreeFormConnectionImpl) {
				FreeFormConnectionImpl freeForm = (FreeFormConnectionImpl) connection;
				EList<org.eclipse.graphiti.mm.algorithms.styles.Point> bendpoints = freeForm.getBendpoints();
				for (org.eclipse.graphiti.mm.algorithms.styles.Point bp : bendpoints) {
					addBendPoint(freeForm, point);
				}
			}

			point = DcFactory.eINSTANCE.createPoint();
			graphicsAlgorithm = connection.getEnd().getGraphicsAlgorithm();
			if (graphicsAlgorithm != null) {
				point.setX(graphicsAlgorithm.getX());
				point.setY(graphicsAlgorithm.getY());
			} else {
				point.setX(connection.getEnd().getParent().getGraphicsAlgorithm().getX());
				point.setY(connection.getEnd().getParent().getGraphicsAlgorithm().getY());
			}
			waypoint.add(point);

		} catch (IOException e) {
			Activator.logError(e);
		}
	}

	static void addBendPoint(FreeFormConnectionImpl freeForm, Point point) {
		freeForm.getBendpoints().add(Graphiti.getGaService().createPoint((int) point.getX(), (int) point.getY()));
	}

	public static BPMNShape createDIShape(Shape shape, BaseElement elem, int x, int y, int w, int h,
			IFeatureProvider fp, Diagram diagram) {

		EList<EObject> businessObjects = Graphiti.getLinkService().getLinkForPictogramElement(diagram)
				.getBusinessObjects();
		BPMNShape bpmnShape = null;

		for (EObject eObject : businessObjects) {
			if (eObject instanceof BPMNDiagram) {
				BPMNDiagram bpmnDiagram = (BPMNDiagram) eObject;

				bpmnShape = BpmnDiFactory.eINSTANCE.createBPMNShape();
//				bpmnShape.setId(EcoreUtil.generateUUID());
				bpmnShape.setBpmnElement(elem);
				Bounds bounds = DcFactory.eINSTANCE.createBounds();
				bounds.setX(x);
				bounds.setY(y);
				bounds.setWidth(w);
				bounds.setHeight(h);
				bpmnShape.setBounds(bounds);

				List<DiagramElement> elements = bpmnDiagram.getPlane().getPlaneElement();
				elements.add(bpmnShape);
				ModelUtil.setID(shape);

				fp.link(shape, new Object[] { elem, bpmnShape });
				break;
			}
		}

		return bpmnShape;
	}
}
