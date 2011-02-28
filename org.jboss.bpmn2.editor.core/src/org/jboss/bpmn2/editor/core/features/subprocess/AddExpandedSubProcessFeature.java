package org.jboss.bpmn2.editor.core.features.subprocess;

import static org.jboss.bpmn2.editor.core.features.subprocess.SubProcessFeatureContainer.TRIGGERED_BY_EVENT;

import org.eclipse.bpmn2.SubProcess;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.styles.AdaptedGradientColoredAreas;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.PredefinedColoredAreas;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class AddExpandedSubProcessFeature extends AbstractAddFeature {
	
	protected FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};
	
	public AddExpandedSubProcessFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    public boolean canAdd(IAddContext context) {
		boolean isSubProcess = context.getNewObject() instanceof SubProcess;
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = support.isTargetLane(context) && support.isTargetLaneOnTop(context);
		boolean intoParticipant = support.isTargetParticipant(context);
		return isSubProcess && (intoDiagram || intoLane || intoParticipant);
    }

	@Override
    public PictogramElement add(IAddContext context) {
		SubProcess subprocess = (SubProcess) context.getNewObject();

		IGaService gaService = Graphiti.getGaService();
		IPeService peService = Graphiti.getPeService();
		
		int paddingBottom = 18;
		int width = context.getWidth() > 0 ? context.getWidth() : 300;
		int height = context.getHeight() > 0 ? context.getHeight() : 300 + paddingBottom;
		
		ContainerShape containerShape = peService.createContainerShape(context.getTargetContainer(), true);
		Rectangle invisibleRect = gaService.createInvisibleRectangle(containerShape);
		gaService.setLocationAndSize(invisibleRect, context.getX(), context.getY(), width, height);
		
		Shape rectShape = peService.createShape(containerShape, false);
		RoundedRectangle rect = gaService.createRoundedRectangle(rectShape, 5, 5);
		rect.setStyle(StyleUtil.getStyleForClass(getDiagram()));
		AdaptedGradientColoredAreas gradient = PredefinedColoredAreas.getBlueWhiteAdaptions();
		gaService.setRenderingStyle(rect, gradient);
		gaService.setLocationAndSize(rect, 0, 0, width, height - paddingBottom);
		decorateRect(rect);
		link(rectShape, subprocess);
		
		ChopboxAnchor anchor = peService.createChopboxAnchor(containerShape);
		anchor.setReferencedGraphicsAlgorithm(rect);
		
		if (subprocess.eResource() == null) {
			getDiagram().eResource().getContents().add(subprocess);
		}
		
		peService.setPropertyValue(containerShape, TRIGGERED_BY_EVENT, "false");
		link(containerShape, subprocess);
		return containerShape;
    }
	
	protected void decorateRect(RoundedRectangle rect) {}
}