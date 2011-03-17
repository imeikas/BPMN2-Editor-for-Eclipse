package org.jboss.bpmn2.editor.core.features.activity;

import org.eclipse.bpmn2.Activity;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.features.MoveFlowNodeFeature;
import org.jboss.bpmn2.editor.core.features.event.AbstractBoundaryEventOperation;

public class ActivityMoveFeature extends MoveFlowNodeFeature {

	public ActivityMoveFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void postMoveShape(final IMoveShapeContext context) {
		super.postMoveShape(context);
		Activity activity = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), Activity.class);
		new AbstractBoundaryEventOperation(activity, getDiagram()) {

			@Override
			protected void doWork(ContainerShape container) {
				GraphicsAlgorithm ga = container.getGraphicsAlgorithm();

				MoveShapeContext newContext = new MoveShapeContext(container);
				newContext.setDeltaX(context.getDeltaX());
				newContext.setDeltaY(context.getDeltaY());
				newContext.setSourceContainer(context.getSourceContainer());
				newContext.setTargetContainer(context.getTargetContainer());
				newContext.setTargetConnection(context.getTargetConnection());
				newContext.setLocation(ga.getX(), ga.getY());
				newContext.putProperty("activity.move", true);

				IMoveShapeFeature moveFeature = getFeatureProvider().getMoveShapeFeature(newContext);
				if (moveFeature.canMoveShape(newContext)) {
					moveFeature.moveShape(newContext);
				}
			}
		};
	}
}