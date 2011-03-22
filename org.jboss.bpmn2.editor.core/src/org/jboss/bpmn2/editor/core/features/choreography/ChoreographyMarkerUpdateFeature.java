package org.jboss.bpmn2.editor.core.features.choreography;

import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class ChoreographyMarkerUpdateFeature extends AbstractUpdateFeature {

	private final IPeService peService = Graphiti.getPeService();

	public ChoreographyMarkerUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return BusinessObjectUtil.containsElementOfType(context.getPictogramElement(), ChoreographyActivity.class);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		ContainerShape choreographyContainer = (ContainerShape) context.getPictogramElement();
		ChoreographyActivity choreography = (ChoreographyActivity) BusinessObjectUtil.getFirstElementOfType(
				choreographyContainer, ChoreographyActivity.class);

		String loopType = choreography.getLoopType() == null ? "null" : choreography.getLoopType().getName();
		String property = peService.getPropertyValue(choreographyContainer, ChoreographyProperties.CHOREOGRAPHY_MARKER);

		if (!loopType.equals(property)) {
			return Reason.createTrueReason();
		} else {
			return Reason.createFalseReason();
		}
	}

	@Override
	public boolean update(IUpdateContext context) {
		ContainerShape choreographyContainer = (ContainerShape) context.getPictogramElement();
		ChoreographyActivity choreography = (ChoreographyActivity) BusinessObjectUtil.getFirstElementOfType(
				choreographyContainer, ChoreographyActivity.class);

		for (Shape s : peService.getAllContainedShapes(choreographyContainer)) {
			String property = peService.getPropertyValue(s, ChoreographyProperties.CHOREOGRAPHY_MARKER_SHAPE);
			if (property != null && new Boolean(property)) {
				ChoreographyUtil.drawChoreographyLoopType((ContainerShape) s, choreography.getLoopType());
			}
		}

		String loopType = choreography.getLoopType() == null ? "null" : choreography.getLoopType().getName();
		peService.setPropertyValue(choreographyContainer, ChoreographyProperties.CHOREOGRAPHY_MARKER, loopType);

		return true;
	}

}