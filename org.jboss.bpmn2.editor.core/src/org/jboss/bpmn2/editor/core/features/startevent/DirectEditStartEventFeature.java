package org.jboss.bpmn2.editor.core.features.startevent;

import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class DirectEditStartEventFeature extends AbstractDirectEditingFeature {

	public DirectEditStartEventFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public int getEditingType() {
		return TYPE_TEXT;
	}

	@Override
	public String getInitialValue(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		StartEvent eClass = (StartEvent) getBusinessObjectForPictogramElement(pe);
		return eClass.getName();
	}

	@Override
	public void setValue(String value, IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();

		StartEvent start = (StartEvent) getBusinessObjectForPictogramElement(pe);
		start.setName(value);

		updatePictogramElement(((Shape) pe).getContainer());
	}

	@Override
	public boolean canDirectEdit(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pe);
		GraphicsAlgorithm ga = context.getGraphicsAlgorithm();

		return bo instanceof StartEvent && ga instanceof Text;
	}

}
