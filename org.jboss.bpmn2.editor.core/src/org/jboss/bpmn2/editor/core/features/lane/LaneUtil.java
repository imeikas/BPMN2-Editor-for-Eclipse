package org.jboss.bpmn2.editor.core.features.lane;

import org.eclipse.bpmn2.Lane;

public class LaneUtil {
	
	public static boolean isTopLane(Lane lane) {
		return lane.getChildLaneSet() == null || lane.getChildLaneSet().getLanes().isEmpty();
	}
}