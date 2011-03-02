package org.jboss.bpmn2.editor.core.di;

import java.io.IOException;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class DIUtils {
	public static void moveDIShape(Diagram diagram, PictogramElement element, Class clazz) {
		try {
			ModelHandler modelHandler = ModelHandlerLocator.getModelHandler(element.getLink().getBusinessObjects()
					.get(0).eResource());

			EObject be = BusinessObjectUtil.getFirstElementOfType(element, clazz);
			BPMNShape shape = modelHandler.findDIElement(diagram, (BaseElement) be);

			ILocation loc = Graphiti.getLayoutService().getLocationRelativeToDiagram((ContainerShape) element);
			Bounds bounds = shape.getBounds();

			bounds.setX(loc.getX());
			bounds.setY(loc.getY());

			GraphicsAlgorithm graphicsAlgorithm = element.getGraphicsAlgorithm();
			bounds.setHeight(graphicsAlgorithm.getHeight());
			bounds.setWidth(graphicsAlgorithm.getWidth());
		} catch (IOException e) {
			Activator.logError(e);
		}
	}

}
