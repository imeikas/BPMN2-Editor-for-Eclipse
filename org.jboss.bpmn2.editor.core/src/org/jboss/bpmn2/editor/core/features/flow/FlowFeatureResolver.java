package org.jboss.bpmn2.editor.core.features.flow;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.jboss.bpmn2.editor.core.features.FeatureResolver;

public class FlowFeatureResolver implements FeatureResolver {

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof MessageFlow)
			return new AddMessageFlowFeature(fp);
		if (e instanceof SequenceFlow)
			return new AddSequenceFlowFeature(fp);
		return null;
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp, BaseElement e) {
		return null; // NOT SUPPORTED YET
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp, BaseElement e) {
		return null; // NOT SUPPORTED YET
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp, BaseElement e) {
		return null; // NOT SUPPORTED YET
	}

	@Override
    public List<ICreateConnectionFeature> getCreateConnectionFeatures(IFeatureProvider fp) {
		List<ICreateConnectionFeature> list = new ArrayList<ICreateConnectionFeature>();
		list.add(new CreateMessageFlowFeature(fp));
		list.add(new CreateSequenceFlowFeature(fp));
	    return list;
    }

	@Override
    public List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) {
	    return new ArrayList<ICreateFeature>();
    }

	@Override
    public IMoveShapeFeature getMoveFeature(IFeatureProvider fp, BaseElement e) {
	    return null; // NOT SUPPORTED YET
    }

	@Override
    public IResizeShapeFeature getResizeFeature(IFeatureProvider fp, BaseElement e) {
	    return null; // NOT SUPPORTED YET
    }
}
