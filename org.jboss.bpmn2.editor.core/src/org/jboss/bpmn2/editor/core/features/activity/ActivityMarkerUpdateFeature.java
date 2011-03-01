package org.jboss.bpmn2.editor.core.features.activity;

import org.eclipse.bpmn2.Activity;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.StyleUtil;
import org.jboss.bpmn2.editor.core.features.ShapeUtil.Compensation;

public class ActivityMarkerUpdateFeature extends AbstractUpdateFeature {

	public static String IS_COMPENSATE_PROPERTY = "marker.compensate";

	public ActivityMarkerUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		Object bo = getBusinessObjectForPictogramElement(context.getPictogramElement());
		return bo != null && bo instanceof Activity && context.getPictogramElement() instanceof ContainerShape;
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		IPeService peService = Graphiti.getPeService();
		PictogramElement element = context.getPictogramElement();
		String property = peService.getPropertyValue(element, IS_COMPENSATE_PROPERTY);
		if(property == null) {
			return Reason.createFalseReason();
		}
		Activity activity = (Activity) getBusinessObjectForPictogramElement(context.getPictogramElement());
		boolean isCompensate = Boolean.parseBoolean(property);
		return activity.isIsForCompensation() != isCompensate ? Reason.createTrueReason() : Reason.createFalseReason();
	}

	@Override
	public boolean update(IUpdateContext context) {
		IPeService peService = Graphiti.getPeService();
		ContainerShape container = (ContainerShape) context.getPictogramElement();
		Activity activity = (Activity) getBusinessObjectForPictogramElement(context.getPictogramElement());

		if (activity.isIsForCompensation()) {
			Compensation compensation = ShapeUtil.createActivityMarkerCompensate(container);
			compensation.arrow1.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			compensation.arrow2.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		} else {
			ShapeUtil.removerActivityMarker(container, ShapeUtil.ACTIVITY_MARKER_COMPENSATE);
		}

		peService.setPropertyValue(container, IS_COMPENSATE_PROPERTY, Boolean.toString(activity.isIsForCompensation()));
		return true;
	}
}