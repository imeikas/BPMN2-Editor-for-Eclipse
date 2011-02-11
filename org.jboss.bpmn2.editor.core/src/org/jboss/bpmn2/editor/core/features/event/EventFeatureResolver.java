package org.jboss.bpmn2.editor.core.features.event;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.jboss.bpmn2.editor.core.features.DirectEditFlowElementFeature;
import org.jboss.bpmn2.editor.core.features.FeatureResolver;

public class EventFeatureResolver implements FeatureResolver {

	@Override
	public AbstractAddShapeFeature getAddFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof StartEvent) {
			return new AddStartEventFeature(fp);
		} else if (e instanceof EndEvent) {
			return new AddEndEventFeature(fp);
		} else {
			return null;
		}
	}

	@Override
	public AbstractCreateFeature getCreateFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof StartEvent) {
			return new CreateStartEventFeature(fp);
		} else if (e instanceof EndEvent) {
			return new CreateEndEventFeature(fp);
		} else {
			return null;
		}
	}

	@Override
	public AbstractDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof Event)
			return new DirectEditFlowElementFeature(fp);
		else
			return null;
	}

	@Override
	public AbstractLayoutFeature getLayoutFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof Event)
			return new LayoutEventFeature(fp);
		else
			return null;
	}

	@Override
	public AbstractUpdateFeature getUpdateFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof Event)
			return new UpdateEventFeature(fp);
		else
			return null;
	}
}
