package org.jboss.bpmn2.editor.ui.features.activity.subprocess;

import static org.jboss.bpmn2.editor.ui.features.activity.subprocess.SubProcessFeatureContainer.TRIGGERED_BY_EVENT;

import org.eclipse.bpmn2.Activity;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.services.Graphiti;
import org.jboss.bpmn2.editor.core.features.activity.AbstractAddActivityFeature;
import org.jboss.bpmn2.editor.core.utils.GraphicsUtil;

public class AddExpandedSubProcessFeature extends AbstractAddActivityFeature {

	public AddExpandedSubProcessFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
	protected void hook(Activity activity, ContainerShape container, IAddContext context, int width, int height) {
		super.hook(activity, container, context, width, height);
		Graphiti.getPeService().setPropertyValue(container, TRIGGERED_BY_EVENT, "false");
	}
	
	@Override
    protected int getWidth() {
	    return GraphicsUtil.SUB_PROCEESS_DEFAULT_WIDTH;
    }
	
	@Override
    protected int getHeight() {
	    return GraphicsUtil.SUB_PROCESS_DEFAULT_HEIGHT;
    }
}