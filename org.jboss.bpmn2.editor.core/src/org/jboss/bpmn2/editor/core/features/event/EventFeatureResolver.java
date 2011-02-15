package org.jboss.bpmn2.editor.core.features.event;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.jboss.bpmn2.editor.core.features.DirectEditFlowElementFeature;
import org.jboss.bpmn2.editor.core.features.FeatureResolver;
import org.jboss.bpmn2.editor.core.features.MoveFlowNodeFeature;

public class EventFeatureResolver implements FeatureResolver {

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof StartEvent)
			return new AddStartEventFeature(fp);
		if (e instanceof EndEvent)
			return new AddEndEventFeature(fp);
		if (e instanceof IntermediateThrowEvent)
			return new AddIntermediateThrowEventFeature(fp);
		if (e instanceof IntermediateCatchEvent)
			return new AddIntermediateCatchEventFeature(fp);
		return null;
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof Event)
			return new DirectEditFlowElementFeature(fp);
		else
			return null;
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof Event)
			return new LayoutEventFeature(fp);
		else
			return null;
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof Event)
			return new UpdateEventFeature(fp);
		else
			return null;
	}

	@Override
	public List<ICreateConnectionFeature> getCreateConnectionFeatures(IFeatureProvider fp) {
		return new ArrayList<ICreateConnectionFeature>();
	}

	@Override
	public List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) {
		List<ICreateFeature> list = new ArrayList<ICreateFeature>();
		list.add(new CreateStartEventFeature(fp));
		list.add(new CreateEndEventFeature(fp));
		list.add(new CreateIntermediateThrowEventFeature(fp));
		list.add(new CreateIntermediateCatchEventFeature(fp));
		return list;
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp, BaseElement e) {
		if(e instanceof Event) {
			return new MoveFlowNodeFeature(fp);
		}
		return null;
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp, BaseElement e) {
		return null; // NOT SUPPORTED YET
	}
}
