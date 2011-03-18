package org.jboss.bpmn2.editor.core.features.participant;

import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.features.DefaultBpmnMoveFeature;

public class MoveParticipantFeature extends DefaultBpmnMoveFeature {

	public MoveParticipantFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		EObject container = context.getShape().eContainer();
		if (container instanceof PictogramElement) {
			PictogramElement containerElem = (PictogramElement) container;
			if (BusinessObjectUtil.containsElementOfType(containerElem, ChoreographyActivity.class)) {
				return false;
			}
		}
		return super.canMoveShape(context);
	}
}