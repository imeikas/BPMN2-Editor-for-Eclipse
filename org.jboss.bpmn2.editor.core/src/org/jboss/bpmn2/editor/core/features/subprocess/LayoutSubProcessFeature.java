package org.jboss.bpmn2.editor.core.features.subprocess;

import java.util.Iterator;

import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
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
		ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
		GraphicsAlgorithm containerGa = containerShape.getGraphicsAlgorithm();
		IGaService gaService = Graphiti.getGaService();
		
		Iterator<Shape> iterator = Graphiti.getPeService().getAllContainedShapes(containerShape).iterator();
		
		RoundedRectangle rect = (RoundedRectangle) iterator.next().getGraphicsAlgorithm();
		gaService.setSize(rect, containerGa.getWidth(), containerGa.getHeight() - 18);
		
		layoutInRectangle(rect);
		
		while (iterator.hasNext()) {
	        Shape shape = (Shape) iterator.next();
	        Object o = getBusinessObjectForPictogramElement(shape);
	        if(o != null && o instanceof BoundaryEvent) {
	        	//layoutPictogramElement(shape);
	        }
        }
		
	    return true;
    }
	
	protected void layoutInRectangle(RoundedRectangle rect) {
	}
}