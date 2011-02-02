package org.jboss.bpmn2.editor.core.features.endevent;

import java.io.IOException;

import org.eclipse.bpmn2.EndEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;

public class CreateEndEventFeature extends AbstractCreateFeature {

	public CreateEndEventFeature(IFeatureProvider fp) {
		super(fp, "End Event", "Indicates the end of a process or choreography");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		return context.getTargetContainer() instanceof Diagram;
	}

	@Override
	public Object[] create(ICreateContext context) {
		EndEvent end = null;
		try {
			ModelHandler mh = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
			end = mh.addFlowElement(ModelHandler.FACTORY.createEndEvent());
			end.setName("End");
		} catch (IOException e) {
			Activator.logError(e);
		}

		addGraphicalRepresentation(context, end);

		return new Object[] { end };
	}

	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_END_EVENT;
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); // FIXME
	}
}
