package org.jboss.bpmn2.editor.core.features;

import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class UpdateTaskFeature extends AbstractUpdateFeature {

	public UpdateTaskFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof Task;
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {

		String name = getShapeName(context);

		String businessName = getBusinessName(context);

		boolean businessObjectHasName = name == null && businessName != null;
		boolean businessObjectHasDifferentName = name != null && !name.equals(businessName);

		boolean updateNeeded = businessObjectHasName || businessObjectHasDifferentName;
		if (updateNeeded) {
			return Reason.createTrueReason("Name out of date");
		}
		return Reason.createFalseReason();
	}

	private String getBusinessName(IUpdateContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getPictogramElement());

		if (bo instanceof Task) {
			Task task = (Task) bo;
			return task.getName();
		}
		return null;
	}

	private String getShapeName(IUpdateContext context) {
		String name = null;

		PictogramElement pe = context.getPictogramElement();
		if (pe instanceof ContainerShape) {
			ContainerShape cs = (ContainerShape) pe;
			for (Shape shape : cs.getChildren()) {
				if (shape.getGraphicsAlgorithm() instanceof Text) {
					Text text = (Text) shape.getGraphicsAlgorithm();
					name = text.getValue();
				}
			}
		}
		return name;
	}

	@Override
	public boolean update(IUpdateContext context) {
		PictogramElement pe = context.getPictogramElement();
		if (pe instanceof ContainerShape) {
			ContainerShape cs = (ContainerShape) pe;
			for (Shape shape : cs.getChildren()) {
				if (shape.getGraphicsAlgorithm() instanceof Text) {
					Text text = (Text) shape.getGraphicsAlgorithm();
					text.setValue(getBusinessName(context));
					return true;
				}
			}
		}

		return false;
	}

}
