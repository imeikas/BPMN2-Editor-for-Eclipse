package org.jboss.bpmn2.editor.core.features.participant;

import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class ResizeParticipantFeature extends DefaultResizeShapeFeature {

	public ResizeParticipantFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canResizeShape(IResizeShapeContext context) {
		EObject container = context.getShape().eContainer();
		if (container instanceof PictogramElement) {
			PictogramElement containerElem = (PictogramElement) container;
			if (BusinessObjectUtil.containsElementOfType(containerElem, ChoreographyActivity.class)) {
				return false;
			}
		}
		return super.canResizeShape(context);
	}
}