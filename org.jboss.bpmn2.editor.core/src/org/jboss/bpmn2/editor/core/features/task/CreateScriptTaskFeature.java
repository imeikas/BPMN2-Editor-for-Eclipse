package org.jboss.bpmn2.editor.core.features.task;

import org.eclipse.bpmn2.ScriptTask;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public class CreateScriptTaskFeature extends AbstractCreateFlowElementFeature<ScriptTask> {

	public CreateScriptTaskFeature(IFeatureProvider fp) {
	    super(fp, "Script Task", "Task executed by a business process engine");
    }

	@Override
    protected ScriptTask createFlowElement(ICreateContext context) {
		ScriptTask task = ModelHandler.FACTORY.createScriptTask();
		task.setName("Script Task");
	    return task;
    }
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_SCRIPT_TASK;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); //FIXME
	}
}