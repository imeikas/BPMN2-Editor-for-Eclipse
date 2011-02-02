package org.jboss.bpmn2.editor.core.features.task;

import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.AdaptedGradientColoredAreas;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.PredefinedColoredAreas;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class AddTaskFeature extends AbstractAddShapeFeature {

	public AddTaskFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean isTask = context.getNewObject() instanceof Task;
		boolean intoDiagram = context.getTargetContainer() instanceof Diagram;
		return isTask && intoDiagram;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		Task addedTask = (Task) context.getNewObject();
		Diagram targetDiagram = (Diagram) context.getTargetContainer();

		// CONTAINER SHAPE WITH ROUNDED RECTANGLE
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(targetDiagram, true);

		// define a default size for the shape
		int width = 100;
		int height = 50;
		IGaService gaService = Graphiti.getGaService();

		{
			// create and set graphics algorithm
			RoundedRectangle roundedRectangle = gaService.createRoundedRectangle(containerShape, 5, 5);
			roundedRectangle.setStyle(StyleUtil.getStyleForClass(getDiagram()));

			AdaptedGradientColoredAreas gradient = PredefinedColoredAreas.getBlueWhiteAdaptions();
			gaService.setRenderingStyle(roundedRectangle, gradient);

			gaService.setLocationAndSize(roundedRectangle, context.getX(), context.getY(), width, height);

			if (addedTask.eResource() == null) {
				getDiagram().eResource().getContents().add(addedTask);
			}
			// create link and wire it
			link(containerShape, addedTask);

		}

		// SHAPE WITH TEXT
		{
			// create shape for text
			Shape shape = peCreateService.createShape(containerShape, false);
			// create and set text graphics algorithm
			Text text = gaService.createDefaultText(shape, addedTask.getName());
			text.setStyle(StyleUtil.getStyleForText(getDiagram()));
			text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
			text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
			text.getFont().setBold(true);
			gaService.setLocationAndSize(text, 0, 0, width, 20);

			// create link and wire it
			link(shape, addedTask);
		}

		peCreateService.createChopboxAnchor(containerShape);
		return containerShape;
	}

}
