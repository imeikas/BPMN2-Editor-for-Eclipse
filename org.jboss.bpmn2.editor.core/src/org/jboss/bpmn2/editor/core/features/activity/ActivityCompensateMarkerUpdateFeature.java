package org.jboss.bpmn2.editor.core.features.activity;

import org.eclipse.bpmn2.Activity;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.ShapeUtil.Compensation;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class ActivityCompensateMarkerUpdateFeature extends AbstractMarkerUpdateFeature {
	
	public static String IS_COMPENSATE_PROPERTY = "marker.compensate";
	
	public ActivityCompensateMarkerUpdateFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    String getPropertyKey() {
	    return IS_COMPENSATE_PROPERTY;
    }

	@Override
	boolean isPropertyChanged(Activity activity, String propertyValue) {
		return activity.isIsForCompensation() != new Boolean(propertyValue);
	}
	
	@Override
	void doUpdate(Activity activity, ContainerShape markerContainer) {
		if (activity.isIsForCompensation()) {
			Compensation compensation = ShapeUtil.createActivityMarkerCompensate(markerContainer);
			compensation.arrow1.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			compensation.arrow2.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
		} else {
			ShapeUtil.clearActivityMarker(markerContainer, ShapeUtil.ACTIVITY_MARKER_COMPENSATE);
		}
	}
	
	@Override
    String convertPropertyToString(Activity activity) {
	    return Boolean.toString(activity.isIsForCompensation());
    }
}