package org.jboss.bpmn2.editor.core.features;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.AbstractText;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public abstract class AbstractBaseElementUpdateFeature extends AbstractUpdateFeature {

	protected FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return BusinessObjectUtil.getFirstElementOfType(element, BaseElement.class);
		}
	};

	public AbstractBaseElementUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		String shapeValue = support.getShapeValue(context);
		String businessValue = support.getBusinessValue(context);

		boolean updateNeeded = shapeValue != null && !shapeValue.equals(businessValue);

		if (updateNeeded) {
			return Reason.createTrueReason("Name out of date");
		}

		return Reason.createFalseReason();
	}

	@Override
	public boolean update(IUpdateContext context) {
		// TODO here it should get an updated picture from the element controller

		PictogramElement pe = context.getPictogramElement();
		if (pe instanceof ContainerShape) {
			ContainerShape cs = (ContainerShape) pe;
			for (Shape shape : cs.getChildren()) {
				if (shape.getGraphicsAlgorithm() instanceof AbstractText) {
					AbstractText text = (AbstractText) shape.getGraphicsAlgorithm();
					text.setValue(support.getBusinessValue(context));
					return true;
				}
			}
		}
		return false;
	}

}
