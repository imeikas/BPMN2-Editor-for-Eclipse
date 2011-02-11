package org.jboss.bpmn2.editor.core.features.lane;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;

public class MoveFromParticipantToDiagramFeature extends MoveLaneFeature {

	public MoveFromParticipantToDiagramFeature(IFeatureProvider fp) {
	    super(fp);
    }
	
	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		return true;
	}
}
