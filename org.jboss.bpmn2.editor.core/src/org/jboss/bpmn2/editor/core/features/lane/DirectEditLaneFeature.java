package org.jboss.bpmn2.editor.core.features.lane;

import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class DirectEditLaneFeature extends AbstractDirectEditingFeature {

	public DirectEditLaneFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    public int getEditingType() {
		return TYPE_TEXT;
    }

	@Override
    public String getInitialValue(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		Lane lane = (Lane) getBusinessObjectForPictogramElement(pe);
		return lane.getName();
    }

	@Override
    public void setValue(String value, IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		Lane lane = (Lane) getBusinessObjectForPictogramElement(pe);
		lane.setName(value);
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
		PictogramElement pe = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pe);
		GraphicsAlgorithm ga = context.getGraphicsAlgorithm();
		return bo instanceof Lane && ga instanceof Text;
	}
	
	
}
