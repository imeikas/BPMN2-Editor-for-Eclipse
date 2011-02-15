package org.jboss.bpmn2.editor.core.features.gateway;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.EventBasedGateway;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.InclusiveGateway;
import org.eclipse.bpmn2.ParallelGateway;
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
import org.jboss.bpmn2.editor.core.features.MoveFlowNodeFeature;

public class GatewayFeatureResolver implements FeatureResolver {

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp, BaseElement e) {
		if (e instanceof ExclusiveGateway)
			return new AddExclusiveGatewayFeature(fp);
		if (e instanceof InclusiveGateway)
			return new AddInclusiveGatewayFeature(fp);
		if (e instanceof ParallelGateway)
			return new AddParallelGatewayFeature(fp);
		if (e instanceof EventBasedGateway)
			return new AddEventBasedGatewayFeature(fp);
		return null;
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp, BaseElement e) {
		return null; // NOT YET SUPPORTED
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp, BaseElement e) {
		return null; // NOT YET SUPPORTED
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp, BaseElement e) {
		return null; // NOT YET SUPPORTED
	}

	@Override
    public List<ICreateConnectionFeature> getCreateConnectionFeatures(IFeatureProvider fp) {
	    return new ArrayList<ICreateConnectionFeature>();
    }

	@Override
    public List<ICreateFeature> getCreateFeatures(IFeatureProvider fp) {
		List<ICreateFeature> list = new ArrayList<ICreateFeature>();
		list.add(new CreateExclusiveGatewayFeature(fp));
		list.add(new CreateInclusiveGatewayFeature(fp));
		list.add(new CreateParallelGatewayFeature(fp));
		list.add(new CreateEventBasedGatewayFeature(fp));
	    return list;
    }

	@Override
    public IMoveShapeFeature getMoveFeature(IFeatureProvider fp, BaseElement e) {
		if(e instanceof Gateway) {
			return new MoveFlowNodeFeature(fp);
		}
	    return null;
    }

	@Override
    public IResizeShapeFeature getResizeFeature(IFeatureProvider fp, BaseElement e) {
	    return null; // NOT YET SUPPORTED
    }
}
