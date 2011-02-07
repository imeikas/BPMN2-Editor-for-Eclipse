package org.jboss.bpmn2.editor.core.features.lane;

import java.io.IOException;
import java.util.List;

import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.ILayoutService;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;

public class MoveFromLaneFeature extends MoveLaneFeature {

	public MoveFromLaneFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canMoveShape(IMoveShapeContext context) {
		if (context.getTargetContainer().equals(getDiagram()))
			return true;
		return false;
	}

	@Override
	protected void internalMove(IMoveShapeContext context) {
		modifyModelStructure(context);
		resizeParentLane(context);
	}

	private void modifyModelStructure(IMoveShapeContext context) {
		Lane parentLane = (Lane) getBusinessObjectForPictogramElement(context.getSourceContainer());
		Lane movedLane = (Lane) getBusinessObjectForPictogramElement(context.getShape());
		parentLane.getChildLaneSet().getLanes().remove(movedLane);
        try {
        	ModelHandler mh = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
	        mh.addLane(movedLane);
        } catch (IOException e) {
        	Activator.logError(e);
        }
    }

	private void resizeParentLane(IMoveShapeContext context) {
		ILayoutService layout = Graphiti.getLayoutService();
		ContainerShape sourceContainer = context.getSourceContainer();
		GraphicsAlgorithm sourceGa = sourceContainer.getGraphicsAlgorithm();
		if (!isEmptyAfterRemoveLane(sourceContainer)) {
			IDimension sourceSize = layout.calculateSize(sourceGa);
			IDimension shapeSize = layout.calculateSize(context.getShape().getGraphicsAlgorithm());
			int newHeight = sourceSize.getHeight() - shapeSize.getHeight() + 1;
			Graphiti.getLayoutService().setSize(sourceGa, sourceSize.getWidth(), newHeight);
		}
	}

	private boolean isEmptyAfterRemoveLane(ContainerShape container) {
		Lane lane = (Lane) getBusinessObjectForPictogramElement(container);
		List<Lane> lanes = lane.getChildLaneSet().getLanes();
		return lanes.size() == 0;
	}
}