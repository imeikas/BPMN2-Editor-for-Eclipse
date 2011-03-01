package org.jboss.bpmn2.editor.core.features.activity;

import java.util.Iterator;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.ShapeUtil.Compensation;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class ActivityCompensateMarkerUpdateFeature extends AbstractUpdateFeature {

	public static String IS_COMPENSATE_PROPERTY = "marker.compensate";

	public ActivityCompensateMarkerUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		Object bo = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), BaseElement.class);
		return bo != null && bo instanceof Activity && context.getPictogramElement() instanceof ContainerShape;
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		IPeService peService = Graphiti.getPeService();
		PictogramElement element = context.getPictogramElement();
		String property = peService.getPropertyValue(element, IS_COMPENSATE_PROPERTY);
		if (property == null) {
			return Reason.createFalseReason();
		}
		Activity activity = (Activity) BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(),
				Activity.class);
		boolean isCompensate = Boolean.parseBoolean(property);
		return activity.isIsForCompensation() != isCompensate ? Reason.createTrueReason() : Reason.createFalseReason();
	}

	@Override
	public boolean update(IUpdateContext context) {
		IPeService peService = Graphiti.getPeService();
		ContainerShape container = (ContainerShape) context.getPictogramElement();
		Activity activity = (Activity) BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(),
				Activity.class);

		ContainerShape markerContainer = null;
		Iterator<Shape> iterator = peService.getAllContainedShapes(container).iterator();
		while (iterator.hasNext()) {
			Shape shape = iterator.next();
			String property = peService.getPropertyValue(shape, ShapeUtil.ACTIVITY_MARKER_CONTAINER);
			if (property != null && new Boolean(property)) {
				markerContainer = (ContainerShape) shape;
				break;
			}
		}

		if (activity.isIsForCompensation()) {
			Compensation compensation = ShapeUtil.createActivityMarkerCompensate(markerContainer);
			compensation.arrow1.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			compensation.arrow2.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		} else {
			ShapeUtil.removerActivityMarker(markerContainer, ShapeUtil.ACTIVITY_MARKER_COMPENSATE);
		}

		peService.setPropertyValue(container, IS_COMPENSATE_PROPERTY, Boolean.toString(activity.isIsForCompensation()));
		return true;
	}
}