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
package org.eclipse.bpmn2.modeler.core.utils;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.ModelHandlerLocator;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.features.BusinessObjectUtil;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.ITargetContext;
import org.eclipse.graphiti.mm.algorithms.AbstractText;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class FeatureSupport {

	public static boolean isTargetSubProcess(ITargetContext context) {
		return BusinessObjectUtil.containsElementOfType(context.getTargetContainer(), SubProcess.class);
	}

	public static boolean isTargetLane(ITargetContext context) {
		return isLane(context.getTargetContainer());
	}

	public static boolean isLane(PictogramElement element) {
		return BusinessObjectUtil.containsElementOfType(element, Lane.class);
	}

	public static boolean isTargetParticipant(ITargetContext context) {
		return isParticipant(context.getTargetContainer());
	}

	public static boolean isParticipant(PictogramElement element) {
		return BusinessObjectUtil.containsElementOfType(element, Participant.class);
	}

	public static boolean isLaneOnTop(Lane lane) {
		return lane.getChildLaneSet() == null || lane.getChildLaneSet().getLanes().isEmpty();
	}

	public static boolean isTargetLaneOnTop(ITargetContext context) {
		Lane lane = BusinessObjectUtil.getFirstElementOfType(context.getTargetContainer(), Lane.class);
		return lane.getChildLaneSet() == null || lane.getChildLaneSet().getLanes().isEmpty();
	}

	/**
	 * Use ModelHandler.getInstance(diagram) instead
	 * 
	 * @param diagram
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	public static ModelHandler getModelHanderInstance(Diagram diagram) throws IOException {
		return ModelHandlerLocator.getModelHandler(diagram.eResource());
	}

	public static void redraw(ContainerShape container) {
		ContainerShape root = getRootContainer(container);
		resizeRecursively(root);
		postResizeFixLenghts(root);
		updateDI(root);
	}

	private static void updateDI(ContainerShape root) {
		Diagram diagram = Graphiti.getPeService().getDiagramForPictogramElement(root);

		Class<?> instanceClass = BusinessObjectUtil.getFirstElementOfType(root, BaseElement.class).eClass()
				.getInstanceClass();
		DIUtils.updateDIShape(root);
	}

	private static ContainerShape getRootContainer(ContainerShape container) {
		ContainerShape parent = container.getContainer();
		EObject bo = BusinessObjectUtil.getFirstElementOfType(parent, BaseElement.class);
		if (bo != null && (bo instanceof Lane || bo instanceof Participant)) {
			return getRootContainer(parent);
		}
		return container;
	}

	private static Dimension resize(ContainerShape container) {
		EObject elem = BusinessObjectUtil.getFirstElementOfType(container, BaseElement.class);
		IGaService service = Graphiti.getGaService();
		int height = 0;
		int width = container.getGraphicsAlgorithm().getWidth() - 15;

		EList<Shape> children = container.getChildren();
		ECollections.sort(children, new SiblingLaneComparator());
		for (Shape s : children) {
			Object bo = BusinessObjectUtil.getFirstElementOfType(s, BaseElement.class);
			if (bo != null && (bo instanceof Lane || bo instanceof Participant) && !bo.equals(elem)) {
				GraphicsAlgorithm ga = s.getGraphicsAlgorithm();
				service.setLocation(ga, 15, height);
				height += ga.getHeight() - 1;
				if (ga.getWidth() >= width) {
					width = ga.getWidth();
				} else {
					service.setSize(ga, width, ga.getHeight());
				}
			}
		}

		GraphicsAlgorithm ga = container.getGraphicsAlgorithm();

		if (height == 0) {
			return new Dimension(ga.getWidth(), ga.getHeight());
		} else {
			int newWidth = width + 15;
			int newHeight = height + 1;
			service.setSize(ga, newWidth, newHeight);

			for (Shape s : children) {
				GraphicsAlgorithm childGa = s.getGraphicsAlgorithm();
				if (childGa instanceof Text) {
					s.getGraphicsAlgorithm().setHeight(newHeight);
				} else if (childGa instanceof Polyline) {
					Polyline line = (Polyline) childGa;
					Point firstPoint = line.getPoints().get(0);
					Point newPoint = service.createPoint(firstPoint.getX(), newHeight);
					line.getPoints().set(1, newPoint);
				}
			}

			return new Dimension(newWidth, newHeight);
		}
	}

	private static Dimension resizeRecursively(ContainerShape root) {
		BaseElement elem = BusinessObjectUtil.getFirstElementOfType(root, BaseElement.class);
		List<Dimension> dimensions = new ArrayList<Dimension>();
		IGaService service = Graphiti.getGaService();
		int foundContainers = 0;

		for (Shape s : root.getChildren()) {
			Object bo = BusinessObjectUtil.getFirstElementOfType(s, BaseElement.class);
			if (checkForResize(elem, s, bo)) {
				foundContainers += 1;
				Dimension d = resizeRecursively((ContainerShape) s);
				if (d != null) {
					dimensions.add(d);
				}
			}
		}

		if (dimensions.isEmpty()) {
			GraphicsAlgorithm ga = root.getGraphicsAlgorithm();
			for (Shape s : root.getChildren()) {
				GraphicsAlgorithm childGa = s.getGraphicsAlgorithm();
				if (childGa instanceof Text) {
					s.getGraphicsAlgorithm().setHeight(ga.getHeight());
				} else if (childGa instanceof Polyline) {
					Polyline line = (Polyline) childGa;
					Point firstPoint = line.getPoints().get(0);
					Point newPoint = service.createPoint(firstPoint.getX(), ga.getHeight());
					line.getPoints().set(1, newPoint);
				}
			}
			return new Dimension(ga.getWidth(), ga.getHeight());
		}

		if (foundContainers > 0) {
			return resize(root);
		}

		return getMaxDimension(dimensions);
	}

	/**
	 * One can only resize lanes and participants
	 */
	private static boolean checkForResize(BaseElement currentBo, Shape s, Object bo) {
		if (!(s instanceof ContainerShape)) {
			return false;
		}
		if (bo == null) {
			return false;
		}
		if (!(bo instanceof Lane || bo instanceof Participant)) {
			return false;
		}
		return !bo.equals(currentBo);
	}

	private static Dimension getMaxDimension(List<Dimension> dimensions) {
		if (dimensions.isEmpty()) {
			return null;
		}
		int height = 0;
		int width = 0;

		for (Dimension d : dimensions) {
			height += d.height;
			if (d.width > width) {
				width = d.width;
			}
		}

		return new Dimension(width, height);
	}

	private static void postResizeFixLenghts(ContainerShape root) {
		IGaService service = Graphiti.getGaService();
		BaseElement elem = BusinessObjectUtil.getFirstElementOfType(root, BaseElement.class);
		int width = root.getGraphicsAlgorithm().getWidth() - 15;

		for (Shape s : root.getChildren()) {
			Object o = BusinessObjectUtil.getFirstElementOfType(s, BaseElement.class);
			if (checkForResize(elem, s, o)) {
				GraphicsAlgorithm ga = s.getGraphicsAlgorithm();
				service.setSize(ga, width, ga.getHeight());
				postResizeFixLenghts((ContainerShape) s);
			}
		}
	}

	public static String getShapeValue(IPictogramElementContext context) {
		String value = null;

		PictogramElement pe = context.getPictogramElement();
		if (pe instanceof ContainerShape) {
			ContainerShape cs = (ContainerShape) pe;
			for (Shape shape : cs.getChildren()) {
				if (shape.getGraphicsAlgorithm() instanceof AbstractText) {
					AbstractText text = (AbstractText) shape.getGraphicsAlgorithm();
					value = text.getValue();
				}
			}
		}
		return value;
	}

	public static String getBusinessValue(IPictogramElementContext context) {
		Object o = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), BaseElement.class);
		if (o instanceof FlowElement) {
			FlowElement e = (FlowElement) o;
			return e.getName();
		} else if (o instanceof TextAnnotation) {
			TextAnnotation a = (TextAnnotation) o;
			return a.getText();
		} else if (o instanceof Participant) {
			Participant p = (Participant) o;
			return p.getName();
		} else if (o instanceof Lane) {
			Lane l = (Lane) o;
			return l.getName();
		}
		return null;
	}

	public static Participant getTargetParticipant(ITargetContext context, ModelHandler handler) {
		if (context.getTargetContainer() instanceof Diagram) {
			return handler.getInternalParticipant();
		}

		Object bo = BusinessObjectUtil.getFirstElementOfType(context.getTargetContainer(), BaseElement.class);

		if (bo instanceof Participant) {
			return (Participant) bo;
		}

		return handler.getParticipant(bo);
	}

	public static Shape getShape(ContainerShape container, String property, String expectedValue) {
		IPeService peService = Graphiti.getPeService();
		Iterator<Shape> iterator = peService.getAllContainedShapes(container).iterator();
		while (iterator.hasNext()) {
			Shape shape = iterator.next();
			String value = peService.getPropertyValue(shape, property);
			if (value != null && value.equals(expectedValue)) {
				return shape;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends EObject> T getChildElementOfType(PictogramElement container, String property, String expectedValue, Class<T> clazz) {
		IPeService peService = Graphiti.getPeService();
		Iterator<PictogramElement> iterator = peService.getAllContainedPictogramElements(container).iterator();
		while (iterator.hasNext()) {
			PictogramElement pe = iterator.next();
			String value = peService.getPropertyValue(pe, property);
			if (value != null && value.equals(expectedValue) && clazz.isInstance(pe)) {
				return (T) pe;
			}
		}
		return null;
	}
}
