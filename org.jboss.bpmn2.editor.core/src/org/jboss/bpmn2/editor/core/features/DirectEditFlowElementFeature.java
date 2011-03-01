package org.jboss.bpmn2.editor.core.features;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class DirectEditFlowElementFeature extends AbstractDirectEditingFeature {

	public DirectEditFlowElementFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public int getEditingType() {
		return TYPE_TEXT;
	}

	@Override
	public String getInitialValue(IDirectEditingContext context) {
		return getBusinessObject(context).getName();
	}

	@Override
	public void setValue(String value, IDirectEditingContext context) {
		getBusinessObject(context).setName(value);
		PictogramElement e = context.getPictogramElement();
		updatePictogramElement(((Shape) e).getContainer());
	}

	@Override
	public boolean canDirectEdit(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		Object bo = BusinessObjectUtil.getFirstElementOfType(pe, BaseElement.class);
		return bo != null && bo instanceof FlowElement && context.getGraphicsAlgorithm() instanceof Text;
	}

	private FlowElement getBusinessObject(IDirectEditingContext context) {
		return (FlowElement) BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), BaseElement.class);
	}
}
