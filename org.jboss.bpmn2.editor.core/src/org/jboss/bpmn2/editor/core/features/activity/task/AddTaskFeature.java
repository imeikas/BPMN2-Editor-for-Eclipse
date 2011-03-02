package org.jboss.bpmn2.editor.core.features.activity.task;

import org.eclipse.bpmn2.Activity;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.StyleUtil;
import org.jboss.bpmn2.editor.core.features.activity.AbstractAddActivityFeature;

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
		Text text = gaService.createDefaultText(textShape, activity.getName());
		gaService.setLocationAndSize(text, 0, 0, width, 20);
		text.setStyle(StyleUtil.getStyleForText(getDiagram()));
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
		text.getFont().setBold(true);
		link(textShape, activity);
	}
	
	@Override
    protected int getWidth() {
	    return ShapeUtil.TASK_DEFAULT_WIDTH;
    }

	@Override
    protected int getHeight() {
	    return ShapeUtil.TASK_DEFAULT_HEIGHT;
    }
}