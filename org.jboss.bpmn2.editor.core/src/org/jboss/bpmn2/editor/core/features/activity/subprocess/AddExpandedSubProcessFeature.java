package org.jboss.bpmn2.editor.core.features.activity.subprocess;

import static org.jboss.bpmn2.editor.core.features.activity.subprocess.SubProcessFeatureContainer.TRIGGERED_BY_EVENT;

import org.eclipse.bpmn2.Activity;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.services.Graphiti;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.activity.AbstractAddActivityFeature;

public class AddExpandedSubProcessFeature extends AbstractAddActivityFeature {

	public AddExpandedSubProcessFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    protected int getWidth() {
	    return ShapeUtil.SUB_PROCEESS_DEFAULT_WIDTH;
    }
	
	@Override
	protected void hook(Activity activity, ContainerShape container, IAddContext context, int width, int height) {
		Graphiti.getPeService().setPropertyValue(container, TRIGGERED_BY_EVENT, "false");
	}
	
	@Override
    protected int getHeight() {
	    return ShapeUtil.SUB_PROCESS_DEFAULT_HEIGHT;
    }
	
//	protected FeatureSupport support = new FeatureSupport() {
//		@Override
//		public Object getBusinessObject(PictogramElement element) {
//			return getBusinessObjectForPictogramElement(element);
//		}
//	};
//	
//	public AddExpandedSubProcessFeature(IFeatureProvider fp) {
//	    super(fp);
//    }
//
//	@Override
//    public boolean canAdd(IAddContext context) {
//		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
//		boolean intoLane = support.isTargetLane(context) && support.isTargetLaneOnTop(context);
//		boolean intoParticipant = support.isTargetParticipant(context);
//		return intoDiagram || intoLane || intoParticipant;
//    }
//
//	@Override
//    public PictogramElement add(IAddContext context) {
//		SubProcess subprocess = (SubProcess) context.getNewObject();
//
//		IGaService gaService = Graphiti.getGaService();
//		IPeService peService = Graphiti.getPeService();
//		
//		int paddingBottom = 18;
//		int width = context.getWidth() > 0 ? context.getWidth() : 300;
//		int height = context.getHeight() > 0 ? context.getHeight() : 300 + paddingBottom;
//		
//		ContainerShape containerShape = peService.createContainerShape(context.getTargetContainer(), true);
//		Rectangle invisibleRect = gaService.createInvisibleRectangle(containerShape);
//		gaService.setLocationAndSize(invisibleRect, context.getX(), context.getY(), width, height);
//		
//		Shape rectShape = peService.createShape(containerShape, false);
//		RoundedRectangle rect = gaService.createRoundedRectangle(rectShape, 5, 5);
//		rect.setStyle(StyleUtil.getStyleForClass(getDiagram()));
//		AdaptedGradientColoredAreas gradient = PredefinedColoredAreas.getBlueWhiteAdaptions();
//		gaService.setRenderingStyle(rect, gradient);
//		gaService.setLocationAndSize(rect, 0, 0, width, height - paddingBottom);
//		decorateRect(rect);
//		link(rectShape, subprocess);
//		
//		ChopboxAnchor anchor = peService.createChopboxAnchor(containerShape);
//		anchor.setReferencedGraphicsAlgorithm(rect);
//		
//		if (subprocess.eResource() == null) {
//			getDiagram().eResource().getContents().add(subprocess);
//		}
//		
//		peService.setPropertyValue(containerShape, TRIGGERED_BY_EVENT, "false");
//		link(containerShape, subprocess);
//		return containerShape;
//    }
//	
//	protected void decorateRect(RoundedRectangle rect) {}
}