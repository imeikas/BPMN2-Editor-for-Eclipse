package org.jboss.bpmn2.editor.core.features.task;

import org.eclipse.bpmn2.Task;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class DirectEditTaskFeature extends AbstractDirectEditingFeature {

	public DirectEditTaskFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public int getEditingType() {
		return TYPE_TEXT;
	}

	@Override
	public String getInitialValue(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		Task eClass = (Task) getBusinessObjectForPictogramElement(pe);
		return eClass.getName();
	}

	@Override
	public void setValue(String value, IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();

		Task task = (Task) getBusinessObjectForPictogramElement(pe);
		task.setName(value);

		updatePictogramElement(((Shape) pe).getContainer());
	}

	@Override
	public String checkValueValid(String value, IDirectEditingContext context) {
		if (value.length() < 1) {
			return "Please enter any text as Task name.";
		} else if (value.contains("\n")) {
			return "Line breakes are not allowed in Task names.";
		}

		// null means, that the value is valid
		return null;

	}

	@Override
	public boolean canDirectEdit(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pe);
		GraphicsAlgorithm ga = context.getGraphicsAlgorithm();

		return bo instanceof Task && ga instanceof Text;
	}

}
