package org.jboss.bpmn2.editor.ui.features.activity;

import java.util.Collection;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.impl.MoveShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.features.MoveFlowNodeFeature;

public class ActivityMoveFeature extends MoveFlowNodeFeature {

	public ActivityMoveFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void postMoveShape(IMoveShapeContext context) {
		super.postMoveShape(context);
		Activity activity = BusinessObjectUtil.getFirstElementOfType2(context.getPictogramElement(), Activity.class);

		IPeService peService = Graphiti.getPeService();
		Collection<PictogramElement> elements = peService.getAllContainedPictogramElements(getDiagram());
		for (PictogramElement e : elements) {
			BoundaryEvent boundaryEvent = BusinessObjectUtil.getFirstElementOfType2(e, BoundaryEvent.class);
			if (boundaryEvent != null && activity.getBoundaryEventRefs().contains(boundaryEvent)) {
				ContainerShape container = (ContainerShape) e;
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
		}
	}
}