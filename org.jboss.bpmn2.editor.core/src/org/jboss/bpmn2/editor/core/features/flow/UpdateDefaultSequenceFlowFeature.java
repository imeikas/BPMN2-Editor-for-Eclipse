package org.jboss.bpmn2.editor.core.features.flow;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.ComplexGateway;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.InclusiveGateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class UpdateDefaultSequenceFlowFeature extends AbstractUpdateFeature {

	public UpdateDefaultSequenceFlowFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		FlowNode node = (FlowNode) BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(),
		        FlowNode.class);
		return node != null && isDefaultAttributeSupported(node);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(IUpdateContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean isDefaultAttributeSupported(FlowNode node) {
		if (node instanceof Activity)
			return true;
		if (node instanceof ExclusiveGateway || node instanceof InclusiveGateway || node instanceof ComplexGateway)
			return true;
		return false;
	}
}