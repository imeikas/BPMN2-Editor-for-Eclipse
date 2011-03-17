package org.jboss.bpmn2.editor.core.features.event;

import java.util.Collection;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public abstract class AbstractBoundaryEventOperation {

	public AbstractBoundaryEventOperation(Activity activity, Diagram diagram) {
		IPeService peService = Graphiti.getPeService();
		Collection<PictogramElement> elements = peService.getAllContainedPictogramElements(diagram);
		for (PictogramElement e : elements) {
			BoundaryEvent boundaryEvent = BusinessObjectUtil.getFirstElementOfType(e, BoundaryEvent.class);
			if (boundaryEvent != null && activity.getBoundaryEventRefs().contains(boundaryEvent)) {
				ContainerShape container = (ContainerShape) e;
				doWork(container);
			}
		}
	}

	protected abstract void doWork(ContainerShape container);
}