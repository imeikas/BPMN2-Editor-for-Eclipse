package org.jboss.bpmn2.editor.core.features.participant;

import org.eclipse.bpmn2.Participant;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class DirectEditParticipantFeature extends AbstractDirectEditingFeature {

	public DirectEditParticipantFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public int getEditingType() {
		return TYPE_TEXT;
	}

	@Override
	public String getInitialValue(IDirectEditingContext context) {
		Participant participant = (Participant) BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(),
				Participant.class);
		return participant.getName();
	}

	@Override
	public void setValue(String value, IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		Participant participant = (Participant) BusinessObjectUtil.getFirstElementOfType(pe, Participant.class);
		participant.setName(value);
		updatePictogramElement(((Shape) pe).getContainer());
	}

	@Override
	public String checkValueValid(String value, IDirectEditingContext context) {
		if (value.length() < 1) {
			return "Please enter any text as Pool name.";
		} else if (value.contains("\n")) {
			return "Line breakes are not allowed in Pool names.";
		}
		return null;
	}

	@Override
	public boolean canDirectEdit(IDirectEditingContext context) {
		Object bo = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), Participant.class);
		GraphicsAlgorithm ga = context.getGraphicsAlgorithm();
		return bo instanceof Participant && ga instanceof Text;
	}
}
