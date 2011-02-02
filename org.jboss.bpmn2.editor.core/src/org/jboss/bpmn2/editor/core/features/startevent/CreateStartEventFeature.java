package org.jboss.bpmn2.editor.core.features.startevent;

import java.io.IOException;

import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;

public class CreateStartEventFeature extends AbstractCreateFeature {

	private static final String FEATURE_NAME = "Start Event";
	private static final String FEATURE_DESCRIPTION = "Indicates the start of a process or choreography";
	private static final String NAME_ON_DIAGRAM = "Start";

	public CreateStartEventFeature(IFeatureProvider fp) {
		super(fp, FEATURE_NAME, FEATURE_DESCRIPTION);
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		return context.getTargetContainer() instanceof Diagram;
	}

	@Override
	public Object[] create(ICreateContext context) {
		StartEvent start = null;
		try {
			ModelHandler mh = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
			start = mh.addFlowElement(ModelHandler.FACTORY.createStartEvent());
			start.setName(NAME_ON_DIAGRAM);
		} catch (IOException e) {
			Activator.logError(e);
		}
		
		addGraphicalRepresentation(context, start);
		
		return new Object[] { start };
	}
	
	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_START_EVENT;
	};
	
	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); //FIXME
	}
}
