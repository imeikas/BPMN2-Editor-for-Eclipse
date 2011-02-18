package org.jboss.bpmn2.editor.core.features.event.definitions;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.StyleUtil;
import org.jboss.bpmn2.editor.core.features.event.definitions.EventDefinitionSupport.EventWithDefinitions;

public abstract class AbstractAddEventDefinitionFeature extends AbstractAddShapeFeature {

	protected EventDefinitionSupport support = new EventDefinitionSupport();

	public AbstractAddEventDefinitionFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
		return support.isValidTarget(bo);
	}

	@Override
	public PictogramElement add(IAddContext context) {
		Event event = (Event) getBusinessObjectForPictogramElement(context.getTargetContainer());
		ContainerShape container = context.getTargetContainer();
		EventWithDefinitions definitions = support.create(event);
		int size = definitions.getEventDefinitions().size();
		
		if (size > 1) {
			if(ShapeUtil.clearEvent(container)) {
				Shape multipleShape = Graphiti.getPeService().createShape(container, false);
				drawForEvent(event, multipleShape);
				link(multipleShape, definitions.getEventDefinitions().toArray(new EventDefinition[size]));
			}
		} else {
			Shape addedShape = getDecorationAlgorithm(event).draw(container);
			link(addedShape, context.getNewObject());
		}
		
		return null;
	}

	protected abstract DecorationAlgorithm getDecorationAlgorithm(Event event);
	
	private void drawForEvent(Event event, Shape shape) {
		Polygon pentagon = ShapeUtil.createEventPentagon(shape);
		pentagon.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));

		if (event instanceof ThrowEvent) {
			pentagon.setBackground(manageColor(StyleUtil.CLASS_FOREGROUND));
		} else {
			pentagon.setFilled(false);
		}
	}
}