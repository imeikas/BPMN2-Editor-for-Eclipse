package org.jboss.bpmn2.editor.core.features.lane;

import java.io.IOException;

import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;

public class CreateLaneFeature extends AbstractCreateFeature {

	public CreateLaneFeature(IFeatureProvider fp) {
	    super(fp, "Lane", "A sub-partition in a process that helps to organize and categorize activities");
    }

	@Override
    public boolean canCreate(ICreateContext context) {
		return context.getTargetContainer() instanceof Diagram;
	}

	@Override
    public Object[] create(ICreateContext context) {
		Lane l = null;
		try {
	        ModelHandler mh = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
	        l = mh.addLane();
	        l.setName("Lane");
		} catch (IOException e) {
			Activator.logError(e);
		}
		addGraphicalRepresentation(context, l);
		return new Object[] { l };
    }
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_LANE;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); // FIXME
	}
}
