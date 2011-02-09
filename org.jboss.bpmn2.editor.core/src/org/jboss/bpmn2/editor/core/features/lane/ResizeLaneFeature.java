package org.jboss.bpmn2.editor.core.features.lane;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public class ResizeLaneFeature extends DefaultResizeShapeFeature {

	private FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};

	public ResizeLaneFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		boolean isLane = support.isLane(context.getPictogramElement());
		if (!isLane)
			return false;

		boolean isParentLane = support.isLane(((ContainerShape) context.getPictogramElement()).getContainer());
		if (!isParentLane)
			return true;

		if (context.getHeight() == -1 && context.getWidth() == -1)
			return true;
		
		GraphicsAlgorithm ga = ((ContainerShape) context.getPictogramElement()).getGraphicsAlgorithm();
		
		boolean higher = context.getHeight() > ga.getHeight(); 
		boolean wider = context.getWidth() > ga.getWidth();
		
		return (wider && higher) || (wider ^ higher);
	}
}
