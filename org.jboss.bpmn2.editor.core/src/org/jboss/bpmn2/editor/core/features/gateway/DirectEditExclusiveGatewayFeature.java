package org.jboss.bpmn2.editor.core.features.gateway;

import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class DirectEditExclusiveGatewayFeature extends AbstractDirectEditingFeature {

	public DirectEditExclusiveGatewayFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public int getEditingType() {
		return TYPE_TEXT;
	}

	@Override
	public String getInitialValue(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		ExclusiveGateway eClass = (ExclusiveGateway) BusinessObjectUtil.getFirstElementOfType(pe,
				ExclusiveGateway.class);
		return eClass.getName();
	}

	@Override
	public void setValue(String value, IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();

		ExclusiveGateway gateway = (ExclusiveGateway) BusinessObjectUtil.getFirstElementOfType(pe,
				ExclusiveGateway.class);
		;
		gateway.setName(value);

		updatePictogramElement(((Shape) pe).getContainer());
	}

	@Override
	public boolean canDirectEdit(IDirectEditingContext context) {
		Object bo = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), ExclusiveGateway.class);
		GraphicsAlgorithm ga = context.getGraphicsAlgorithm();

		return bo instanceof ExclusiveGateway && ga instanceof Text;
	}
}
