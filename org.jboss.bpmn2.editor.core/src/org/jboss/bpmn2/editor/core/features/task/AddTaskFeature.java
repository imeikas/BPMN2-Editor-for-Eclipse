package org.jboss.bpmn2.editor.core.features.task;

import static org.jboss.bpmn2.editor.core.features.task.SizeConstants.HEIGHT;
import static org.jboss.bpmn2.editor.core.features.task.SizeConstants.PADDING_BOTTOM;
import static org.jboss.bpmn2.editor.core.features.task.SizeConstants.WIDTH;

import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.AdaptedGradientColoredAreas;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.PredefinedColoredAreas;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class AddTaskFeature extends AbstractAddShapeFeature {
	
	protected FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};
	
	public AddTaskFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    public boolean canAdd(IAddContext context) {
		boolean isTask = context.getNewObject() instanceof Task;
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = support.isTargetLane(context) && support.isTargetLaneOnTop(context);
		boolean intoParticipant = support.isTargetParticipant(context);
		return isTask && (intoDiagram || intoLane || intoParticipant);
    }

	@Override
    public PictogramElement add(IAddContext context) {
		Task addedTask = (Task) context.getNewObject();

		IGaService gaService = Graphiti.getGaService();
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(context.getTargetContainer(), true);

		Rectangle invisibleRectangle = gaService.createInvisibleRectangle(containerShape);
		gaService.setLocationAndSize(invisibleRectangle, context.getX(), context.getY(), getWidth(), HEIGHT + PADDING_BOTTOM);
		
		Shape taskShape = peCreateService.createShape(containerShape, false);
		RoundedRectangle roundedRectangle = gaService.createRoundedRectangle(taskShape, 5, 5);
		gaService.setLocationAndSize(roundedRectangle, 0, 0, getWidth(), HEIGHT);
		roundedRectangle.setStyle(StyleUtil.getStyleForClass(getDiagram()));
		AdaptedGradientColoredAreas gradient = PredefinedColoredAreas.getBlueWhiteAdaptions();
		gaService.setRenderingStyle(roundedRectangle, gradient);
		link(taskShape, addedTask);
		decorateTask(roundedRectangle, context);
		
		if (addedTask.eResource() == null) {
			getDiagram().eResource().getContents().add(addedTask);
		}

		Shape textShape = peCreateService.createShape(containerShape, false);
		Text text = gaService.createDefaultText(textShape, addedTask.getName());
		gaService.setLocationAndSize(text, 0, 0, getWidth(), 20);
		text.setStyle(StyleUtil.getStyleForText(getDiagram()));
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
		text.getFont().setBold(true);
		link(textShape, addedTask);
		
		link(containerShape, addedTask);
		
		ChopboxAnchor anchor = peCreateService.createChopboxAnchor(containerShape);
		anchor.setReferencedGraphicsAlgorithm(roundedRectangle);
		
		layoutPictogramElement(containerShape);
		return containerShape;
    }
	
	protected void decorateTask(RoundedRectangle rect, IAddContext context) {}
	
	protected int getWidth() {
		return WIDTH;
	}
}
