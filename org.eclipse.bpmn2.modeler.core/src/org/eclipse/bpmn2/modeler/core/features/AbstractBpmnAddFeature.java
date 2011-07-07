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
package org.eclipse.bpmn2.modeler.core.features;

import java.io.IOException;
import java.util.List;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.bpmn2.modeler.core.Activator;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.dc.DcFactory;
import org.eclipse.dd.dc.Point;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public abstract class AbstractBpmnAddFeature extends AbstractAddShapeFeature {

	public AbstractBpmnAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	protected void createDIShape(Shape gShape, BaseElement elem) {
		try {
			BPMNShape shape = (BPMNShape) ModelHandlerLocator.getModelHandler(getDiagram().eResource()).findDIElement(
					getDiagram(), elem);
			createDIShape(gShape, elem, shape);
		} catch (IOException e) {
			Activator.logError(e);
		}
	}

	protected void createDIShape(Shape gShape, BaseElement elem, BPMNShape shape) {
		ILocation loc = Graphiti.getLayoutService().getLocationRelativeToDiagram(gShape);
		if (shape == null) {
			EList<EObject> businessObjects = Graphiti.getLinkService().getLinkForPictogramElement(getDiagram())
					.getBusinessObjects();
			for (EObject eObject : businessObjects) {
				if (eObject instanceof BPMNDiagram) {
					BPMNDiagram bpmnDiagram = (BPMNDiagram) eObject;

					shape = BpmnDiFactory.eINSTANCE.createBPMNShape();
//					shape.setId(EcoreUtil.generateUUID());
					shape.setBpmnElement(elem);
					Bounds bounds = DcFactory.eINSTANCE.createBounds();
					if (elem instanceof Activity) {
						bounds.setHeight(gShape.getGraphicsAlgorithm().getHeight());
					} else {
						bounds.setHeight(gShape.getGraphicsAlgorithm().getHeight());
					}
					bounds.setWidth(gShape.getGraphicsAlgorithm().getWidth());
					bounds.setX(loc.getX());
					bounds.setY(loc.getY());
					shape.setBounds(bounds);

					addShape(shape, bpmnDiagram);
					ModelUtil.setID(shape);
				}
			}
		}
		link(gShape, new Object[] { elem, shape });
	}

	private void addShape(DiagramElement elem, BPMNDiagram bpmnDiagram) {
		List<DiagramElement> elements = bpmnDiagram.getPlane().getPlaneElement();
		elements.add(elem);
	}

	protected void createDIEdge(Connection connection, BaseElement conElement) {
		try {
			BPMNEdge edge = (BPMNEdge) ModelHandlerLocator.getModelHandler(getDiagram().eResource()).findDIElement(
					getDiagram(), conElement);
			createDIEdge(connection, conElement, edge);
		} catch (IOException e) {
			Activator.logError(e);
		}

	}

	protected void createDIEdge(Connection connection, BaseElement conElement, BPMNEdge edge) throws IOException {
		ModelHandler modelHandler = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
		if (edge == null) {
			EList<EObject> businessObjects = Graphiti.getLinkService().getLinkForPictogramElement(getDiagram())
					.getBusinessObjects();
			for (EObject eObject : businessObjects) {
				if (eObject instanceof BPMNDiagram) {
					BPMNDiagram bpmnDiagram = (BPMNDiagram) eObject;

					edge = BpmnDiFactory.eINSTANCE.createBPMNEdge();
//					edge.setId(EcoreUtil.generateUUID());
					edge.setBpmnElement(conElement);

					if (conElement instanceof Association) {
						edge.setSourceElement(modelHandler.findDIElement(getDiagram(),
								((Association) conElement).getSourceRef()));
						edge.setTargetElement(modelHandler.findDIElement(getDiagram(),
								((Association) conElement).getTargetRef()));
					} else if (conElement instanceof MessageFlow) {
						edge.setSourceElement(modelHandler.findDIElement(getDiagram(),
								(BaseElement) ((MessageFlow) conElement).getSourceRef()));
						edge.setTargetElement(modelHandler.findDIElement(getDiagram(),
								(BaseElement) ((MessageFlow) conElement).getTargetRef()));
					} else if (conElement instanceof SequenceFlow) {
						edge.setSourceElement(modelHandler.findDIElement(getDiagram(),
								((SequenceFlow) conElement).getSourceRef()));
						edge.setTargetElement(modelHandler.findDIElement(getDiagram(),
								((SequenceFlow) conElement).getTargetRef()));
					}

					ILocation sourceLoc = Graphiti.getPeService().getLocationRelativeToDiagram(connection.getStart());
					ILocation targetLoc = Graphiti.getPeService().getLocationRelativeToDiagram(connection.getEnd());

					Point point = DcFactory.eINSTANCE.createPoint();
					point.setX(sourceLoc.getX());
					point.setY(sourceLoc.getY());
					edge.getWaypoint().add(point);

					point = DcFactory.eINSTANCE.createPoint();
					point.setX(targetLoc.getX());
					point.setY(targetLoc.getY());
					edge.getWaypoint().add(point);

					addShape(edge, bpmnDiagram);
					ModelUtil.setID(edge);
				}
			}
		}
		link(connection, new Object[] { conElement, edge });
	}
}