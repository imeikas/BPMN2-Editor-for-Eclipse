package org.jboss.bpmn2.editor.core.features.activity.task;

import org.eclipse.bpmn2.Activity;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.features.activity.AbstractAddActivityFeature;
import org.jboss.bpmn2.editor.core.utils.FeatureSupport;
import org.jboss.bpmn2.editor.core.utils.GraphicsUtil;
import org.jboss.bpmn2.editor.core.utils.StyleUtil;

public class AddTaskFeature extends AbstractAddActivityFeature {

	public AddTaskFeature(IFeatureProvider fp) {
	    super(fp);
    }
	
	@Override
	public boolean canAdd(IAddContext context) {
	    return super.canAdd(context) || FeatureSupport.isTargetSubProcess(context);
	}
	
	@Override
	protected void hook(Activity activity, ContainerShape container, IAddContext context, int width, int height) {
		IPeService peService = Graphiti.getPeService();
		IGaService gaService = Graphiti.getGaService();
		
		Shape textShape = peService.createShape(container, false);
		MultiText text = gaService.createDefaultMultiText(textShape, activity.getName());
		int padding = GraphicsUtil.TASK_IMAGE_SIZE;
		gaService.setLocationAndSize(text, 0, padding, width, height - padding);
		text.setStyle(StyleUtil.getStyleForText(getDiagram()));
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_TOP);
		text.getFont().setBold(true);
		link(textShape, activity);
	}
	
	@Override
    protected int getWidth() {
	    return GraphicsUtil.TASK_DEFAULT_WIDTH;
    }

	@Override
    protected int getHeight() {
	    return GraphicsUtil.TASK_DEFAULT_HEIGHT;
    }
}