package org.jboss.bpmn2.editor.core.features.choreography;

import static org.jboss.bpmn2.editor.core.features.choreography.Properties.BOTTOM_BAND;
import static org.jboss.bpmn2.editor.core.features.choreography.Properties.CHOREOGRAPHY_TASK_PROPERTY;
import static org.jboss.bpmn2.editor.core.features.choreography.Properties.INITIATING_PARTICIPANT_REF;
import static org.jboss.bpmn2.editor.core.features.choreography.Properties.PARTICIPANT_REF;
import static org.jboss.bpmn2.editor.core.features.choreography.Properties.PARTICIPANT_REF_ID;
import static org.jboss.bpmn2.editor.core.features.choreography.Properties.TOP_BAND;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.bpmn2.ChoreographyTask;
import org.eclipse.bpmn2.Participant;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.IColorConstant;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.utils.Tuple;

public class UpdateInitiatingParticipantFeature extends AbstractUpdateFeature {

	private static IPeService peService = Graphiti.getPeService();

	public UpdateInitiatingParticipantFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		return BusinessObjectUtil.containsElementOfType(context.getPictogramElement(), ChoreographyTask.class);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {
		ChoreographyTask task = (ChoreographyTask) BusinessObjectUtil.getFirstElementOfType(
		        context.getPictogramElement(), ChoreographyTask.class);

		String property = peService.getPropertyValue(context.getPictogramElement(), INITIATING_PARTICIPANT_REF);
		Participant participant = task.getInitiatingParticipantRef();

		if (property.equals("false") && participant == null) {
			return Reason.createFalseReason();
		}

		if (participant != null && property.equals(participant.getId())) {
			return Reason.createFalseReason();
		} else {
			return Reason.createTrueReason();
		}
	}

	@Override
	public boolean update(IUpdateContext context) {
		ChoreographyTask task = (ChoreographyTask) BusinessObjectUtil.getFirstElementOfType(
		        context.getPictogramElement(), ChoreographyTask.class);

		List<Shape> shapeList = new ArrayList<Shape>();

		Tuple<Shape, Shape> topAndBottomBands = getTopAndBottomBands(context);
		shapeList.add(topAndBottomBands.getFirst());
		shapeList.add(topAndBottomBands.getSecond());
		
		for(Shape shape : peService.getAllContainedShapes((ContainerShape) context.getPictogramElement())) {
			String property = peService.getPropertyValue(shape, PARTICIPANT_REF);
			if(property != null && new Boolean(property)) {
				shapeList.add(shape);
			}
		}

		for (Shape shape : shapeList) {
			if (task.getInitiatingParticipantRef() == null) {
				shape.getGraphicsAlgorithm().setBackground(manageColor(IColorConstant.LIGHT_GRAY));
				continue;
			}

			String id = peService.getPropertyValue(shape, PARTICIPANT_REF_ID);
			if (id.equals(task.getInitiatingParticipantRef().getId())) {
				shape.getGraphicsAlgorithm().setBackground(manageColor(IColorConstant.WHITE));
			} else {
				shape.getGraphicsAlgorithm().setBackground(manageColor(IColorConstant.LIGHT_GRAY));
			}
		}

		String propertyVal = task.getInitiatingParticipantRef() == null ? Boolean.toString(false) : task
		        .getInitiatingParticipantRef().getId();
		peService.setPropertyValue(context.getPictogramElement(), INITIATING_PARTICIPANT_REF, propertyVal);
		return true;
	}

	private Tuple<Shape, Shape> getTopAndBottomBands(IUpdateContext context) {
		Shape top = null;
		Shape bottom = null;
		Iterator<Shape> iterator = peService.getAllContainedShapes((ContainerShape) context.getPictogramElement())
		        .iterator();
		while (iterator.hasNext()) {
			Shape shape = (Shape) iterator.next();
			String property = peService.getPropertyValue(shape, CHOREOGRAPHY_TASK_PROPERTY);
			if (property == null) {
				continue;
			}
			if (property.equals(TOP_BAND)) {
				top = shape;
			} else if (property.equals(BOTTOM_BAND)) {
				bottom = shape;
			}
			if (top != null && bottom != null) {
				break;
			}
		}
		return new Tuple<Shape, Shape>(top, bottom);
	}
}