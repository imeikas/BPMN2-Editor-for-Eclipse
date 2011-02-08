package org.jboss.bpmn2.editor.core.features;

import java.io.IOException;

import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.context.ITargetContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.ILayoutService;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;

public abstract class FeatureSupport {
	
	protected abstract Object getBusinessObject(PictogramElement element);
	
	public boolean isTargetLane(ITargetContext context) {
		return isLane(context.getTargetContainer());
	}
	
	public boolean isLane(PictogramElement element) {
		Object bo = getBusinessObject(element);
		return bo != null && bo instanceof Lane;
	}
	
	public boolean isLaneOnTop(Lane lane) {
		return lane.getChildLaneSet() == null || lane.getChildLaneSet().getLanes().isEmpty();
	}
	
	public boolean isTargetLaneOnTop(ITargetContext context) {
		Lane lane = (Lane) getBusinessObject(context.getTargetContainer());
		return lane.getChildLaneSet() == null || lane.getChildLaneSet().getLanes().isEmpty();
	}
	
	public ModelHandler getModelHanderInstance(Diagram diagram) throws IOException {
		return ModelHandlerLocator.getModelHandler(diagram.eResource());
	}
	
	public boolean isTopLane(Lane lane) {
		return lane.getChildLaneSet() == null || lane.getChildLaneSet().getLanes().isEmpty();
	}
	
	public void resizeLanesRecursively(ContainerShape container, int height, int x, int y) {
		if(container == null) {
			return;
		}
		
		Object bo = getBusinessObject(container);
		if(bo == null || !(bo instanceof Lane)) {
			return;
		}
		
		IGaService gaService = Graphiti.getGaService();
		ILayoutService layoutService = Graphiti.getLayoutService();
		GraphicsAlgorithm ga =  container.getGraphicsAlgorithm();
		
		gaService.setSize(ga, ga.getWidth(), ga.getHeight() + height);
		
		for(Shape s : container.getChildren()) {
			ILocation location = layoutService.getLocationRelativeToDiagram(s);
			if(location.getY() >= y && location.getX() != x) {
				GraphicsAlgorithm childGa = s.getGraphicsAlgorithm();
				gaService.setLocation(childGa, childGa.getX(), childGa.getY() + height);
			}
		}
		
		resizeLanesRecursively(container.getContainer(), height, x, y);
	}
	
	public void packLanes(ContainerShape container) {
		if(container == null) {
			return;
		}
		
		Object bo = getBusinessObject(container);
		if(bo == null || !(bo instanceof Lane)) {
			return;
		}
		
		IGaService gaService = Graphiti.getGaService();
		GraphicsAlgorithm ga =  container.getGraphicsAlgorithm();
		
		int childrenHeight = 0;
		for(Shape s : container.getChildren()) {
			GraphicsAlgorithm childGa = s.getGraphicsAlgorithm();

			if(childGa.getY() > childrenHeight) {
				gaService.setLocation(childGa, childGa.getX(), childrenHeight);
			}
			
			Object o = getBusinessObject(s);
			if(o instanceof Lane && !(o.equals(bo))) {
				childrenHeight += childGa.getHeight();
			}
		}
		
		if(container.getChildren().size() > 1 && childrenHeight < ga.getHeight()) {
			gaService.setSize(ga, ga.getWidth(), childrenHeight);
		}
		
		packLanes(container.getContainer());
	}
}
