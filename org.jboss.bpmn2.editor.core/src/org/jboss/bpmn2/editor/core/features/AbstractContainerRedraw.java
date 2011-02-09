package org.jboss.bpmn2.editor.core.features;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

// TODO extract common logic to abstract methods to use redraw with processes also
public abstract class AbstractContainerRedraw<T extends BaseElement> {
	
	private FeatureSupport support;
	
	public AbstractContainerRedraw(FeatureSupport support) {
		this.support = support;
	}
	
	public void redraw(ContainerShape container) {
		ContainerShape root = getRootContainer(container);
		resizeRecursively(root);
		postResizeFixLenghts(root);
	}

	private ContainerShape getRootContainer(ContainerShape container) {
		ContainerShape parent = container.getContainer();
		Object bo = support.getBusinessObject(parent);
		if (bo != null && bo instanceof Lane) {
			return getRootContainer(parent);
		}
		return container;
	}

	private Dimension resize(ContainerShape container) {
		Lane lane = (Lane) support.getBusinessObject(container);
		IGaService service = Graphiti.getGaService();
		int height = 0;
		int width = container.getGraphicsAlgorithm().getWidth() - 15;
		List<GraphicsAlgorithm> gaList = new ArrayList<GraphicsAlgorithm>();
		
		for (Shape s : container.getChildren()) {
			Object bo = support.getBusinessObject(s);
			if (bo != null && bo instanceof Lane && !bo.equals(lane)) {
				GraphicsAlgorithm ga = s.getGraphicsAlgorithm();
				service.setLocation(ga, 15, height);
				height += ga.getHeight() - 1;
				if (ga.getWidth() >= width) {
					width = ga.getWidth();
				} else {
					service.setSize(ga, width, ga.getHeight());
				}
				gaList.add(ga);
			}
		}
		
		GraphicsAlgorithm ga = container.getGraphicsAlgorithm();

		if (height == 0) {
			return new Dimension(ga.getWidth(), ga.getHeight());
		} else {
			int newWidth = width + 15;
			int newHeight = height + 1;
			service.setSize(ga, newWidth, newHeight);
			
			for (Shape s : container.getChildren()) {
				if (s.getGraphicsAlgorithm() instanceof Text) {
					s.getGraphicsAlgorithm().setHeight(newHeight);
				}
			}
			
			return new Dimension(newWidth, newHeight);
		}
	}

	private Dimension resizeRecursively(ContainerShape root) {
		Lane lane = (Lane) support.getBusinessObject(root);
		List<Dimension> dimensions = new ArrayList<Dimension>();
		int foundLanes = 0;

		for (Shape s : root.getChildren()) {
			Object bo = support.getBusinessObject(s);
			if (s instanceof ContainerShape && bo != null && bo instanceof Lane && !bo.equals(lane)) {
				foundLanes += 1;
				Dimension d = resizeRecursively((ContainerShape) s);
				if (d != null) {
					dimensions.add(d);
				}
			}
		}
		
		if(dimensions.isEmpty()) {
			GraphicsAlgorithm ga = root.getGraphicsAlgorithm();
			for (Shape s : root.getChildren()) {
				if (s.getGraphicsAlgorithm() instanceof Text) {
					s.getGraphicsAlgorithm().setHeight(ga.getHeight());
				}
			}
			return new Dimension(ga.getWidth(), ga.getHeight());
		}
		
		if (foundLanes > 0) {
			return resize(root);
		}

		return getMaxDimension(dimensions);
	}

	private Dimension getMaxDimension(List<Dimension> dimensions) {
		if (dimensions.isEmpty()) {
			return null;
		}
		int height = 0;
		int width = 0;

		for (Dimension d : dimensions) {
			height += d.height;
			if (d.width > width) {
				width = d.width;
			}
		}

		return new Dimension(width, height);
	}
	
	private void postResizeFixLenghts(ContainerShape root) {
		IGaService service = Graphiti.getGaService();
		Lane lane = (Lane) support.getBusinessObject(root);
		int width = root.getGraphicsAlgorithm().getWidth() - 15;
		
		for(Shape s : root.getChildren()) {
			Object o = support.getBusinessObject(s);
			if(s instanceof ContainerShape && o != null && o instanceof Lane && !o.equals(lane)) {
				GraphicsAlgorithm ga = s.getGraphicsAlgorithm();
				service.setSize(ga, width, ga.getHeight());
				postResizeFixLenghts((ContainerShape) s);
			}
		}
	}
}
