package org.jboss.bpmn2.editor.core.features;


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
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;
import org.eclipse.graphiti.util.PredefinedColoredAreas;

public class AddTaskFeature extends AbstractAddShapeFeature {
	private static final IColorConstant CLASS_TEXT_FOREGROUND = IColorConstant.BLACK;
	private static final IColorConstant CLASS_FOREGROUND = new ColorConstant(146, 146, 208);
	private static final IColorConstant CLASS_BACKGROUND = new ColorConstant(220, 220, 255);

	public AddTaskFeature(IFeatureProvider fp) {
		super(fp);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canAdd(IAddContext context) {
		  if (context.getNewObject() instanceof Task) {
	            if (context.getTargetContainer() instanceof Diagram) {
	                return true;
	            }
	        }
	        return false;
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
			roundedRectangle.setForeground(manageColor(CLASS_FOREGROUND));
			roundedRectangle.setBackground(manageColor(CLASS_BACKGROUND));

			AdaptedGradientColoredAreas gradient = PredefinedColoredAreas.getBlueWhiteAdaptions();
			
			gaService.setRenderingStyle(roundedRectangle, gradient);
			roundedRectangle.setLineWidth(1);
			gaService.setLocationAndSize(roundedRectangle, context.getX(), context.getY(), width, height);
			// if added Class has no resource we add it to the resource
			// of the diagram
			// in a real scenario the business model would have its own resource
			if (addedTask.eResource() == null) {
				getDiagram().eResource().getContents().add(addedTask);
			}
			// create link and wire it
			link(containerShape, addedTask);
		}
		// SHAPE WITH TEXT
		{
			// create shape for text
			Shape shape = (Shape) peCreateService.createShape(containerShape, false);
			// create and set text graphics algorithm
			Text text = gaService.createDefaultText(shape, addedTask.getName());
			text.setForeground(manageColor(CLASS_TEXT_FOREGROUND));
			text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
			text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
			text.getFont().setBold(true);
			gaService.setLocationAndSize(text, 0, 0, width, 20);

			// create link and wire it
			link(shape, addedTask);
		}

		return containerShape;
	}

}
