package org.jboss.bpmn2.editor.core.features.event;

import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;

public abstract class AbstractLayoutEventFeature extends AbstractLayoutFeature {

	public AbstractLayoutEventFeature(IFeatureProvider fp) {
	    super(fp);
    }
	
	@Override
    public boolean canLayout(ILayoutContext context) {
		PictogramElement pictoElem = context.getPictogramElement();
		if(!(pictoElem instanceof ContainerShape)) {
			return false;
		}
		EList<EObject> businessObjs = pictoElem.getLink().getBusinessObjects();
	    return businessObjs.size() == 1 && isInstanceOf(businessObjs.get(0));
    }
	
	protected abstract boolean isInstanceOf(Object businessObject);

	@Override
    public boolean layout(ILayoutContext context) {
		boolean changed = false;
		
		ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
		GraphicsAlgorithm containerGa = containerShape.getGraphicsAlgorithm();
		IGaService gaService = Graphiti.getGaService();
		
		if(containerGa.getWidth() < ShapeUtil.EVENT_SIZE) {
			containerGa.setWidth(ShapeUtil.EVENT_SIZE);
			changed = true;
		}
		
		if(containerGa.getHeight() < ShapeUtil.EVENT_SIZE + ShapeUtil.EVENT_TEXT_AREA) {
			containerGa.setHeight(ShapeUtil.EVENT_SIZE + ShapeUtil.EVENT_TEXT_AREA);
			changed = true;
		}
		
		int containerWidth = containerGa.getWidth();
		int containerHeight = containerGa.getHeight();
		
		Iterator<Shape> iterator = containerShape.getChildren().iterator();
		while (iterator.hasNext()) {
	        Shape shape = (Shape) iterator.next();
	        GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
	        IDimension size = gaService.calculateSize(ga);

	        if(containerWidth != size.getWidth()) {
	        	gaService.setWidth(ga, containerWidth);
	        	changed = true;
	        }
	        
	        int sizeForEllipse = containerHeight - ShapeUtil.EVENT_TEXT_AREA;
	        if (ga instanceof Ellipse) {
	        	if(sizeForEllipse != ga.getHeight()) {
	        		gaService.setHeight(ga, sizeForEllipse);
	        		changed = true;
	        	}
	        } else if (ga instanceof Text) {
	        	if(sizeForEllipse != ga.getY()) {
	        		gaService.setLocation(ga, 0, sizeForEllipse);
	        		changed = true;
	        	}
	        }
        }
		
	    return changed;
	}

}
