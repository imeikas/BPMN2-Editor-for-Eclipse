package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ITargetContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;

public class AddBoundaryEvent extends AbstractAddShapeFeature {

	public AddBoundaryEvent(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		if (!(context.getNewObject() instanceof BoundaryEvent))
			return false;
		if (!(getBusinessObjectForPictogramElement(context.getTargetContainer()) instanceof Activity))
			return false;
		return true;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		return null;
	}
	
	private Shape createEventShape(IAddContext context) {
		BoundaryEvent event = (BoundaryEvent) context.getNewObject();
		Shape shape = Graphiti.getPeService().createShape(context.getTargetContainer(), false);
		if(event.isCancelActivity()) {
			// 2 lines
		} else {
			// 2 dashes
		}
		return shape;
	}
}