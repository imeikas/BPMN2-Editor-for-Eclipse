package org.jboss.bpmn2.editor.core.features;

import java.io.IOException;

import org.eclipse.bpmn2.Task;
import org.eclipse.emf.common.util.URI;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class CreateTaskFeature extends AbstractCreateFeature {

	public CreateTaskFeature(IFeatureProvider fp) {
		super(fp, "Task", "Create Task");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		return context.getTargetContainer() instanceof Diagram;
	}

	@Override
	public Object[] create(ICreateContext context) {
		final String name = "Task Name";
		URI uri = getDiagram().eResource().getURI();
		uri = uri.trimFragment();
		uri = uri.trimFileExtension();
		uri = uri.appendFileExtension("bpmn2"); //FIXME: move into some Util
		
		ModelHandler mh = null;
		Task x = null;
        try {
	        mh = ModelHandler.getModelHandler(uri);
	        x = mh.createTask();
	        x.setName(name);
        } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
		
		addGraphicalRepresentation(context, x);

		return new Object[] { x };
	}

}
