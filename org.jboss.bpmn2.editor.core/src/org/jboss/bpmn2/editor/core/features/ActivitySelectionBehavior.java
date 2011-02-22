package org.jboss.bpmn2.editor.core.features;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.bpmn2.Activity;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;

public class ActivitySelectionBehavior {

	public static boolean canApplyTo(PictogramElement element) {
		if (element.getLink() == null)
			return false;

		EList<EObject> objects = element.getLink().getBusinessObjects();
		int size = objects.size();
		if (size == 0 || size > 1)
			return false;

		return objects.get(0) instanceof Activity && element instanceof ContainerShape;
	}

	public static GraphicsAlgorithm[] getClickArea(PictogramElement element) {
		Iterator<PictogramElement> iterator = Graphiti.getPeService().getPictogramElementChildren(element).iterator();
		GraphicsAlgorithm[] algorithms = new GraphicsAlgorithm[2];
		algorithms[0] = iterator.next().getGraphicsAlgorithm();
		algorithms[1] = iterator.next().getGraphicsAlgorithm();
		return algorithms;
	}

	public static GraphicsAlgorithm getSelectionBorder(PictogramElement element) {
		Collection<PictogramElement> children = Graphiti.getPeService().getPictogramElementChildren(element);
		PictogramElement first = children.iterator().next();
		return first.getGraphicsAlgorithm();
	}
}