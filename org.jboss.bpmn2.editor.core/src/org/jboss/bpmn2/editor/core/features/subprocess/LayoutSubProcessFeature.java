package org.jboss.bpmn2.editor.core.features.subprocess;

import java.util.Iterator;

import org.eclipse.bpmn2.SubProcess;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

public class LayoutSubProcessFeature extends AbstractLayoutFeature {

	public LayoutSubProcessFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    public boolean canLayout(ILayoutContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getPictogramElement());
	    return bo != null && bo instanceof SubProcess;
    }

	@Override
    public boolean layout(ILayoutContext context) {
		IGaService gaService = Graphiti.getGaService();
		
		Iterator<Shape> iterator = Graphiti.getPeService().getAllContainedShapes((ContainerShape) context.getPictogramElement()).iterator();
		RoundedRectangle rect = (RoundedRectangle) iterator.next().getGraphicsAlgorithm();
		
		layoutInRectangle(rect);
		MultiText text = (MultiText) iterator.next().getGraphicsAlgorithm();
		gaService.setSize(text, rect.getWidth() - 20, rect.getHeight() - 20);
		
	    return true;
    }
	
	protected void layoutInRectangle(RoundedRectangle rect) {
	}
}