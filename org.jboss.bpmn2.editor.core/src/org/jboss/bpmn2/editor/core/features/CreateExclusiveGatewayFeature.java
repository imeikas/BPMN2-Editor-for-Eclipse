package org.jboss.bpmn2.editor.core.features;

import java.io.IOException;

import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class CreateExclusiveGatewayFeature extends AbstractCreateFeature {

	public CreateExclusiveGatewayFeature(IFeatureProvider fp) {
		super(fp, "Exclusive Gateway", "Exclusive decision and merging");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		return context.getTargetContainer() instanceof Diagram;
	}

	@Override
	public Object[] create(ICreateContext context) {
		ExclusiveGateway x = null;;
		
		try {
			ModelHandler mh = ModelHandler.getModelHandler(getDiagram().eResource());
			x = mh.createGateway();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addGraphicalRepresentation(context, x);

		return new Object[] { x };
	}

}
