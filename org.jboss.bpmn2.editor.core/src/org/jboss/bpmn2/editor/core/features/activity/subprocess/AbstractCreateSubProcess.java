package org.jboss.bpmn2.editor.core.features.activity.subprocess;

import org.eclipse.bpmn2.SubProcess;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;

public abstract class AbstractCreateSubProcess extends AbstractCreateFlowElementFeature<SubProcess> {

	public AbstractCreateSubProcess(IFeatureProvider fp, String name, String description) {
	    super(fp, name, description);
    }
	
	protected abstract String getStencilImageId();
	
	@Override
	public String getCreateImageId() {
	    return getStencilImageId();
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId(); // FIXME
	}
}