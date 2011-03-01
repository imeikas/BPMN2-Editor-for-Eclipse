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
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.StyleUtil;
import org.jboss.bpmn2.editor.core.features.activity.AbstractAddActivityFeature;

public class AddTaskFeature extends AbstractAddActivityFeature {

	public AddTaskFeature(IFeatureProvider fp) {
	    super(fp);
    }
	
	@Override
	public boolean canAdd(IAddContext context) {
	    return super.canAdd(context) || support.isTargetSubProcess(context);
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

//	protected FeatureSupport support = new FeatureSupport() {
//		@Override
//		public Object getBusinessObject(PictogramElement element) {
//			return getBusinessObjectForPictogramElement(element);
//		}
//	};
//
//	public AddTaskFeature(IFeatureProvider fp) {
//		super(fp);
//	}
//
//	@Override
//	public boolean canAdd(IAddContext context) {
//		boolean isTask = context.getNewObject() instanceof Task;
//		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
//		boolean intoLane = support.isTargetLane(context) && support.isTargetLaneOnTop(context);
//		boolean intoParticipant = support.isTargetParticipant(context);
//		boolean intoSubProcess = support.isTargetSubProcess(context);
//		return isTask && (intoDiagram || intoLane || intoParticipant || intoSubProcess);
//	}
//
//	@Override
//	public PictogramElement add(IAddContext context) {
//		Task addedTask = (Task) context.getNewObject();
//
//		IGaService gaService = Graphiti.getGaService();
//		IPeCreateService peCreateService = Graphiti.getPeCreateService();
//		ContainerShape containerShape = peCreateService.createContainerShape(context.getTargetContainer(), true);
//
//		int width = context.getWidth() > 0 ? context.getWidth() : getWidth();
//		int height = context.getHeight() > 0 ? context.getHeight() : HEIGHT + PADDING_BOTTOM;
//
//		Rectangle invisibleRectangle = gaService.createInvisibleRectangle(containerShape);
//		gaService.setLocationAndSize(invisibleRectangle, context.getX(), context.getY(), width, height);
//
//		Shape taskShape = peCreateService.createShape(containerShape, false);
//		RoundedRectangle roundedRectangle = gaService.createRoundedRectangle(taskShape, 5, 5);
//		gaService.setLocationAndSize(roundedRectangle, 0, 0, width, height - PADDING_BOTTOM);
//		roundedRectangle.setStyle(StyleUtil.getStyleForClass(getDiagram()));
//		AdaptedGradientColoredAreas gradient = PredefinedColoredAreas.getBlueWhiteAdaptions();
//		gaService.setRenderingStyle(roundedRectangle, gradient);
//		link(taskShape, addedTask);
//		decorateTask(roundedRectangle);
//
//		Shape textShape = peCreateService.createShape(containerShape, false);
//		Text text = gaService.createDefaultText(textShape, addedTask.getName());
//		gaService.setLocationAndSize(text, 0, 0, width, 20);
//		text.setStyle(StyleUtil.getStyleForText(getDiagram()));
//		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
//		text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
//		text.getFont().setBold(true);
//		link(textShape, addedTask);
//
//		link(containerShape, addedTask);
//
//		ChopboxAnchor anchor = peCreateService.createChopboxAnchor(containerShape);
//		anchor.setReferencedGraphicsAlgorithm(roundedRectangle);
//
//		if (addedTask.eResource() == null) {
//			getDiagram().eResource().getContents().add(addedTask);
//		}
//
//		Graphiti.getPeService().setPropertyValue(containerShape, ActivityMarkerUpdateFeature.IS_COMPENSATE_PROPERTY,
//		        Boolean.toString(false));
//		layoutPictogramElement(containerShape);
//		return containerShape;
//	}
//
//	protected void decorateTask(RoundedRectangle rect) {
//	}
//
//	protected int getWidth() {
//		return WIDTH;
//	}
}