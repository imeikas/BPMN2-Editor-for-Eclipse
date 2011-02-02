package org.jboss.bpmn2.editor.core.features.exclusivegateway;

import java.io.IOException;

import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;

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
		try {
			ModelHandler mh = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
			ExclusiveGateway x = mh.createExclusiveGateway();
			addGraphicalRepresentation(context, x);
			return new Object[] { x };
		} catch (IOException e) {
			Activator.logError(e);
		}
		return null;
	}

	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_EXCLUSIVE_GATEWAY;
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); // FIXME
	}
}
