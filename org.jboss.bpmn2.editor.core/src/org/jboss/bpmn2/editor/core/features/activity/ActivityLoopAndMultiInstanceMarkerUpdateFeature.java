package org.jboss.bpmn2.editor.core.features.activity;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.LoopCharacteristics;
import org.eclipse.bpmn2.MultiInstanceLoopCharacteristics;
import org.eclipse.bpmn2.StandardLoopCharacteristics;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.jboss.bpmn2.editor.utils.ShapeUtil;
import org.jboss.bpmn2.editor.utils.StyleUtil;
import org.jboss.bpmn2.editor.utils.ShapeUtil.Loop;
import org.jboss.bpmn2.editor.utils.ShapeUtil.MultiInstance;

public class ActivityLoopAndMultiInstanceMarkerUpdateFeature extends AbstractMarkerUpdateFeature {

	public static String IS_LOOP_OR_MULTI_INSTANCE = "marker.loop.or.multi";

	enum LoopCharacteristicType {
		
		NULL("null"), 
		
		LOOP(StandardLoopCharacteristics.class.getSimpleName()), 
		
		MULTI_PARALLEL(MultiInstanceLoopCharacteristics.class.getSimpleName() + ":parallel"), 
		
		MULTI_SEQUENTIAL(MultiInstanceLoopCharacteristics.class.getSimpleName() + ":sequential");

		private String name;

		private LoopCharacteristicType(String name) {
			this.name = name;
		}

		String getName() {
			return name;
		}
	}

	public ActivityLoopAndMultiInstanceMarkerUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	String getPropertyKey() {
		return IS_LOOP_OR_MULTI_INSTANCE;
	}

	@Override
	boolean isPropertyChanged(Activity activity, String propertyValue) {
		return !getLoopCharacteristicsValue(activity).getName().equals(propertyValue);
	}

	@Override
	void doUpdate(Activity activity, ContainerShape markerContainer) {
		ShapeUtil.clearActivityMarker(markerContainer, ShapeUtil.ACTIVITY_MARKER_LOOP_CHARACTERISTIC);
		switch (getLoopCharacteristicsValue(activity)) {
		case LOOP:
			Loop loop = ShapeUtil.createActivityMarkerStandardLoop(markerContainer);
			loop.circle.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			loop.arrow.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			break;
		case MULTI_PARALLEL:
			MultiInstance multiParallel = ShapeUtil.createActivityMarkerMultiParallel(markerContainer);
			multiParallel.line1.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			multiParallel.line2.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			multiParallel.line3.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			break;
		case MULTI_SEQUENTIAL:
			MultiInstance multiSeq = ShapeUtil.createActivityMarkerMultiSequential(markerContainer);
			multiSeq.line1.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			multiSeq.line2.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			multiSeq.line3.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			break;
		}
	}

	@Override
	String convertPropertyToString(Activity activity) {
		return getLoopCharacteristicsValue(activity).getName();
	}

	public static LoopCharacteristicType getLoopCharacteristicsValue(Activity activity) {
		LoopCharacteristics loopCharacteristics = activity.getLoopCharacteristics();
		LoopCharacteristicType type = LoopCharacteristicType.NULL;

		if (loopCharacteristics != null) {
			if (loopCharacteristics instanceof MultiInstanceLoopCharacteristics) {
				MultiInstanceLoopCharacteristics multi = (MultiInstanceLoopCharacteristics) loopCharacteristics;
				type = multi.isIsSequential() ? LoopCharacteristicType.MULTI_SEQUENTIAL
				        : LoopCharacteristicType.MULTI_PARALLEL;
			} else {
				type = LoopCharacteristicType.LOOP;
			}
		}

		return type;
	}
}