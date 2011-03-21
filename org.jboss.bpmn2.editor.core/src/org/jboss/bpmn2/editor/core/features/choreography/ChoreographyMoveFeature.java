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

import java.util.Collection;
import java.util.List;

import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.features.MoveFlowNodeFeature;

public class ChoreographyMoveFeature extends MoveFlowNodeFeature {

	public ChoreographyMoveFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void postMoveShape(final IMoveShapeContext context) {
		super.postMoveShape(context);
		IPeService peService = Graphiti.getPeService();
		Collection<Shape> shapes = peService.getAllContainedShapes((ContainerShape) context.getShape());
		for (Shape s : shapes) {
			Participant participant = BusinessObjectUtil.getFirstElementOfType(s, Participant.class);
			if (participant != null) {
				ContainerShape container = (ContainerShape) s;
				Polygon polygon = (Polygon) container.getGraphicsAlgorithm();
				Point point = polygon.getPoints().get(0);

				BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(s, BPMNShape.class);
				ILocation loc = Graphiti.getLayoutService().getLocationRelativeToDiagram(context.getShape());
				int yBeforeMove = loc.getY();
				Bounds bounds = bpmnShape.getBounds();
				bounds.setX(loc.getX() + point.getX());
				bounds.setY(yBeforeMove + point.getY());

				List<Connection> connections = peService.getOutgoingConnections(container);
				for (Connection connection : connections) {
					ContainerShape envelope = (ContainerShape) connection.getEnd().getParent();
					GraphicsAlgorithm envelopeGa = envelope.getGraphicsAlgorithm();
					Graphiti.getGaService().setLocation(envelopeGa, envelopeGa.getX() + context.getDeltaX(),
							envelopeGa.getY() + context.getDeltaY());
				}
			}
		}
	}
}