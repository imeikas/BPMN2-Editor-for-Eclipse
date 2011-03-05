package org.jboss.bpmn2.editor.core.features.artifact;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.jboss.bpmn2.editor.core.features.DefaultBpmnMoveFeature;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public class MoveTextAnnotationFeature extends DefaultBpmnMoveFeature {

	public MoveTextAnnotationFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context);
		return intoDiagram || intoLane;
	}

	@Override
	protected void internalMove(IMoveShapeContext context) {
		super.internalMove(context);
		//DIUtils.updateDIShape(getDiagram(), context.getPictogramElement(), TextAnnotation.class);
	}
}