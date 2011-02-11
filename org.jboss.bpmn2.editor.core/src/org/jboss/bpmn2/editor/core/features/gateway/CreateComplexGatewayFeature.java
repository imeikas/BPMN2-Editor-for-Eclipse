package org.jboss.bpmn2.editor.core.features.gateway;

import org.eclipse.bpmn2.ComplexGateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateComplexGatewayFeature extends AbstractCreateFlowElementFeature<ComplexGateway> {

	public CreateComplexGatewayFeature(IFeatureProvider fp) {
	    super(fp, "Complex Gateway", "Used for modeling complex synchronization behavior");
    }

	@Override
    protected ComplexGateway createFlowElement(ICreateContext context) {
	    return ModelHandler.FACTORY.createComplexGateway();
    }
}
