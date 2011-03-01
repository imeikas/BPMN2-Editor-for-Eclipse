package org.jboss.bpmn2.editor.core.features.lane;

import java.io.IOException;

import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class MoveFromLaneToDiagramFeature extends MoveLaneFeature {

	public MoveFromLaneToDiagramFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		return true;
	}

	@Override
	protected void internalMove(IMoveShapeContext context) {
		modifyModelStructure(context);
		support.redraw(context.getSourceContainer());
	}

	private void modifyModelStructure(IMoveShapeContext context) {
		Lane parentLane = (Lane) BusinessObjectUtil.getFirstElementOfType(context.getSourceContainer(), Lane.class);
		Lane movedLane = (Lane) BusinessObjectUtil.getFirstElementOfType(context.getShape(), Lane.class);
		parentLane.getChildLaneSet().getLanes().remove(movedLane);
		try {
			ModelHandler mh = support.getModelHanderInstance(getDiagram());
			mh.laneToTop(movedLane);
		} catch (IOException e) {
			Activator.logError(e);
		}
	}
}