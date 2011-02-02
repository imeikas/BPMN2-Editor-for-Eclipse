package org.jboss.bpmn2.editor.core.features;

import org.eclipse.bpmn2.FlowElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public abstract class AbstractBPMNUpdateFeature extends AbstractUpdateFeature {

	public AbstractBPMNUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		String shapeName = getShapeName(context);
		String businessName = getBusinessName(context);

		boolean businessObjectHasName = shapeName == null && businessName != null;
		boolean businessObjectHasDifferentName = shapeName != null && !shapeName.equals(businessName);

		boolean updateNeeded = businessObjectHasName || businessObjectHasDifferentName;
		if (updateNeeded) {
			return Reason.createTrueReason("Name out of date");
		}
		return Reason.createFalseReason();
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

	protected String getShapeName(IUpdateContext context) {
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

	protected String getBusinessName(IUpdateContext context) {
		Object o = getBusinessObjectForPictogramElement(context.getPictogramElement());
		if (o instanceof FlowElement) {
			FlowElement e = (FlowElement) o;
			return e.getName();
		}
		return null;
	}
}
