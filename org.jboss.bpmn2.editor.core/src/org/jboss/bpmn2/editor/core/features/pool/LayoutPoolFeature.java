package org.jboss.bpmn2.editor.core.features.pool;

import java.util.Iterator;

import org.eclipse.bpmn2.Participant;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

public class LayoutPoolFeature extends AbstractLayoutFeature {

	public LayoutPoolFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    public boolean canLayout(ILayoutContext context) {
		PictogramElement pictoElem = context.getPictogramElement();
		if(!(pictoElem instanceof ContainerShape)) {
			return false;
		}
		EList<EObject> businessObjs = pictoElem.getLink().getBusinessObjects();
	    return businessObjs.size() == 1 && businessObjs.get(0) instanceof Participant;
	}

	@Override
    public boolean layout(ILayoutContext context) {
		boolean changed = false;
		
		ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
		GraphicsAlgorithm containerGa = containerShape.getGraphicsAlgorithm();
		IGaService gaService = Graphiti.getGaService();
		
		int containerHeight = containerGa.getHeight();
		Iterator<Shape> iterator = containerShape.getChildren().iterator();
		while (iterator.hasNext()) {
	        Shape shape = (Shape) iterator.next();
	        GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
	        IDimension size = gaService.calculateSize(ga);
	        if(containerHeight != size.getHeight()) {
	        	if(ga instanceof Polyline) {
	        		Polyline line = (Polyline) ga;
	        		Point firstPoint = line.getPoints().get(0);
	        		Point newPoint = gaService.createPoint(firstPoint.getX(), containerHeight);
	        		line.getPoints().set(1, newPoint);
	        		changed = true;
	        	} else {
	        		gaService.setHeight(ga, containerHeight);
	        		changed = true;
	        	}
	        }
		}
		
	    return changed;
    }

}
