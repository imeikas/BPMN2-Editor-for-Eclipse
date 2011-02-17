package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.ConditionalEventDefinition;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;

public class AddConditionalStartEventFeature extends AbstractAddEventFeature {

	public AddConditionalStartEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void decorateEllipse(Ellipse e) {
		ShapeUtil.createImage(e, ImageProvider.IMG_20_CONDITION);
	}

	@Override
	protected Class<? extends Event> getEventClass() {
		return StartEvent.class;
	}

	public static boolean isDefinitionsMatch(StartEvent e) {
		if (e.getEventDefinitions() == null || e.getEventDefinitions().isEmpty() || e.getEventDefinitions().size() > 1)
			return false;
		return e.getEventDefinitions().get(0) instanceof ConditionalEventDefinition;
	}
}