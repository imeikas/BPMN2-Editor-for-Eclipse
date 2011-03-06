package org.jboss.bpmn2.editor.core.features.choreography;

import static org.jboss.bpmn2.editor.core.features.choreography.Properties.*;

import java.util.Iterator;

import org.eclipse.bpmn2.ChoreographyTask;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class LayoutChoreographyTaskFeature extends AbstractLayoutFeature {

	private static final int H = AddChoreographyTaskFeature.PARTICIPANT_BAND_HEIGHT;
	private static IPeService peService = Graphiti.getPeService();
	private static IGaService gaService = Graphiti.getGaService();

	public LayoutChoreographyTaskFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context) {
		return BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), ChoreographyTask.class) != null;
	}

	@Override
	public boolean layout(ILayoutContext context) {
		ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
		GraphicsAlgorithm parentGa = containerShape.getGraphicsAlgorithm();

		int newWidth = parentGa.getWidth();
		int newHeight = parentGa.getHeight();

		layoutMainComponents(containerShape, newWidth, newHeight);
		layoutParticipantComponents(containerShape, newWidth, newHeight);

		return true;
	}
	
	private void layoutMainComponents(ContainerShape containerShape, int newWidth, int newHeight) {
		Iterator<Shape> iterator = peService.getAllContainedShapes(containerShape).iterator();
		while (iterator.hasNext()) {
			Shape shape = (Shape) iterator.next();
			String property = peService.getPropertyValue(shape, CHOREOGRAPHY_TASK_PROPERTY);
			if (property == null)
				continue;
			GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
			if (property.equals(TOP_BAND)) {
				gaService.setLocationAndSize(ga, 0, 0, newWidth, H);
			} else if (property.equals(TOP_BAND_TEXT)) {
				gaService.setLocationAndSize(ga, 0, 0, newWidth, H);
			} else if (property.equals(BOTTOM_BAND)) {
				gaService.setLocationAndSize(ga, 0, newHeight - H, newWidth, H);
			} else if (property.equals(BOTTOM_BAND_TEXT)) {
				gaService.setLocationAndSize(ga, 0, newHeight - H, newWidth, H);
			} else if (property.equals(BODY_BAND)) {
				gaService.setLocationAndSize(ga, 0, H - 5, newWidth, newHeight - (2 * H) + 10);
			} else if (property.equals(BODY_BAND_TEXT)) {
				gaService.setLocationAndSize(ga, 0, H - 5, newWidth, newHeight - (2 * H) + 10);
			}
		}
	}
	
	private void layoutParticipantComponents(ContainerShape containerShape, int newWidth, int newHeight) {
		Iterator<Shape> iterator = peService.getAllContainedShapes(containerShape).iterator();
		
		int h = 20;
		int topY = AddChoreographyTaskFeature.PARTICIPANT_BAND_HEIGHT - 5;
		int bottomY = newHeight - topY - h;
		int location = 0;
		
		while (iterator.hasNext()) {
			Shape shape = (Shape) iterator.next();
			String property = peService.getPropertyValue(shape, PARTICIPANT_REF);
			if (property != null && new Boolean(property)) {
				GraphicsAlgorithm ga = shape.getGraphicsAlgorithm();
				if(location == 0) {
					gaService.setLocationAndSize(ga, 0, topY, newWidth, h);
					topY += h - 1;
					location += 1;
				} else {
					gaService.setLocationAndSize(ga, 0, bottomY, newWidth, h);
					bottomY -= h - 1;
					location -= 1;
				}
				ga.getGraphicsAlgorithmChildren().get(0).setWidth(newWidth);
			}
		}
	}
}