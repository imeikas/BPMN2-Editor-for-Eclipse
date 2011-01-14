package org.jboss.bpmn2.editor.core.diagram;

import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;
import org.jboss.bpmn2.editor.core.features.AddTaskFeature;
import org.jboss.bpmn2.editor.core.features.CreateTaskFeature;

/**
 * Determines what kinds of business objects can be added to a diagram.
 * 
 * @author imeikas
 * 
 */
public class BPMNFeatureProvider extends DefaultFeatureProvider {

	public BPMNFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);
	}

	@Override
	public IAddFeature getAddFeature(IAddContext context) {
		if (context.getNewObject() instanceof Task)
			return new AddTaskFeature(this);
		return super.getAddFeature(context);
	}

	@Override
	public ICreateFeature[] getCreateFeatures() {
		return new ICreateFeature[] { new CreateTaskFeature(this) };
	}
}
