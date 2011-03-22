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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.di.DIUtils;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.utils.Tuple;

public class ChoreographyUtil {

	public static List<ContainerShape> getParticipantBandContainerShapes(
			ContainerShape choreographyActivityContainerShape) {
		IPeService peService = Graphiti.getPeService();
		List<ContainerShape> containers = new ArrayList<ContainerShape>();
		Collection<Shape> shapes = peService.getAllContainedShapes(choreographyActivityContainerShape);
		for (Shape s : shapes) {
			String property = peService.getPropertyValue(s, ChoreographyProperties.BAND);
			if (property != null && new Boolean(property)) {
				containers.add((ContainerShape) s);
			}
		}
		return containers;
	}

	public static List<BPMNShape> getParicipantBandBpmnShapes(ContainerShape choreographyActivityContainerShape) {
		List<BPMNShape> bpmnShapes = new ArrayList<BPMNShape>();
		List<ContainerShape> containers = getParticipantBandContainerShapes(choreographyActivityContainerShape);
		for (ContainerShape container : containers) {
			BPMNShape bpmnShape = BusinessObjectUtil.getFirstElementOfType(container, BPMNShape.class);
			bpmnShapes.add(bpmnShape);
		}
		return bpmnShapes;
	}

	public static boolean isChoreographyParticipantBand(PictogramElement element) {
		EObject container = element.eContainer();
		if (container instanceof PictogramElement) {
			PictogramElement containerElem = (PictogramElement) container;
			if (BusinessObjectUtil.containsElementOfType(containerElem, ChoreographyActivity.class)) {
				return true;
			}
		}
		return false;
	}

	public static Tuple<List<ContainerShape>, List<ContainerShape>> getTopAndBottomBands(
			List<ContainerShape> participantBands) {
		Collections.sort(participantBands, getParticipantBandComparator());
		int n = participantBands.size();
		int divider = n / 2;
		List<ContainerShape> top = participantBands.subList(0, divider);
		List<ContainerShape> bottom = participantBands.subList(divider, n);
		return new Tuple<List<ContainerShape>, List<ContainerShape>>(top, bottom);
	}

	private static Comparator<ContainerShape> getParticipantBandComparator() {
		return new Comparator<ContainerShape>() {

			@Override
			public int compare(ContainerShape c1, ContainerShape c2) {
				BPMNShape bpmnShape1 = BusinessObjectUtil.getFirstElementOfType(c1, BPMNShape.class);
				Bounds bounds1 = bpmnShape1.getBounds();
				BPMNShape bpmnShape2 = BusinessObjectUtil.getFirstElementOfType(c2, BPMNShape.class);
				Bounds bounds2 = bpmnShape2.getBounds();
				return new Float(bounds1.getY()).compareTo(new Float(bounds2.getY()));
			}

		};
	}

	public static void resizePartipantBandContainerShapes(int w, int h, List<ContainerShape> top,
			List<ContainerShape> bottom, Diagram diagram) {
		IGaService gaService = Graphiti.getGaService();

		int y = 0;
		for (ContainerShape container : top) {
			Bounds bounds = BusinessObjectUtil.getFirstElementOfType(container, BPMNShape.class).getBounds();
			int hAcc = (int) bounds.getHeight();
			gaService.setLocationAndSize(container.getGraphicsAlgorithm(), 0, y, w, hAcc);
			y += hAcc;
			resizeParticipantBandChildren(container, w);
			DIUtils.updateDIShape(container);
		}

		Collections.reverse(bottom); // start from bottom towards center
		y = h;
		for (ContainerShape container : bottom) {
			Bounds bounds = BusinessObjectUtil.getFirstElementOfType(container, BPMNShape.class).getBounds();
			y -= bounds.getHeight();
			gaService.setLocationAndSize(container.getGraphicsAlgorithm(), 0, y, w, (int) bounds.getHeight());
			resizeParticipantBandChildren(container, w);
			DIUtils.updateDIShape(container);
		}
	}

	private static void resizeParticipantBandChildren(ContainerShape container, int w) {
		IGaService gaService = Graphiti.getGaService();
		for (Shape s : container.getChildren()) {
			GraphicsAlgorithm ga = s.getGraphicsAlgorithm();
			if (ga instanceof Text) {
				gaService.setSize(ga, w, ga.getHeight());
			} else if (ga instanceof Rectangle) {
				gaService.setLocation(ga, (w / 2) - (ga.getWidth() / 2), ga.getY());
			}
		}
	}
}