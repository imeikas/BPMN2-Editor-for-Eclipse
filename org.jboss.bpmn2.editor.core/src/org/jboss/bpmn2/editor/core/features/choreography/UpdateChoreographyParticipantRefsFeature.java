package org.jboss.bpmn2.editor.core.features.choreography;

import static org.jboss.bpmn2.editor.core.features.choreography.Properties.BOTTOM_BAND_TEXT;
import static org.jboss.bpmn2.editor.core.features.choreography.Properties.CHOREOGRAPHY_TASK_PROPERTY;
import static org.jboss.bpmn2.editor.core.features.choreography.Properties.PARTICIPANT_REF;
import static org.jboss.bpmn2.editor.core.features.choreography.Properties.PARTICIPANT_REF_NUM;
import static org.jboss.bpmn2.editor.core.features.choreography.Properties.TOP_BAND_TEXT;

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
import org.eclipse.graphiti.mm.Property;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.IColorConstant;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.utils.StyleUtil;
import org.jboss.bpmn2.editor.utils.Tuple;

public class UpdateChoreographyParticipantRefsFeature extends AbstractUpdateFeature {

	private static final IPeService peService = Graphiti.getPeService();
	private static final IGaService gaService = Graphiti.getGaService();
	
	public UpdateChoreographyParticipantRefsFeature(IFeatureProvider fp) {
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

		if (task.getParticipantRefs().size() != getParticipantRefNumber(context)) {
			return Reason.createTrueReason();
		} else {
			return Reason.createFalseReason();
		}
	}

	@Override
	public boolean update(IUpdateContext context) {
		ChoreographyTask task = (ChoreographyTask) BusinessObjectUtil.getFirstElementOfType(
		        context.getPictogramElement(), ChoreographyTask.class);

		clearChoreographyTask(context);
		buildChoreographyTask(task, context);

		peService.setPropertyValue(context.getPictogramElement(), PARTICIPANT_REF_NUM,
		        Integer.toString(task.getParticipantRefs().size()));
		return true;
	}

	private int getParticipantRefNumber(IUpdateContext context) {
		return Integer.parseInt(peService.getPropertyValue(context.getPictogramElement(), PARTICIPANT_REF_NUM));
	}

	private void clearChoreographyTask(IUpdateContext context) {
		List<Shape> toBeRemoved = new ArrayList<Shape>();

		Iterator<Shape> iterator = peService.getAllContainedShapes((ContainerShape) context.getPictogramElement())
		        .iterator();
		while (iterator.hasNext()) {
			Shape shape = (Shape) iterator.next();
			String property = peService.getPropertyValue(shape, PARTICIPANT_REF);
			if (property != null && new Boolean(property)) {
				toBeRemoved.add(shape);
			}
		}

		int size = toBeRemoved.size();
		for (int i = 0; i < size; i++) {
			peService.deletePictogramElement(toBeRemoved.get(i));
		}
	}

	private void buildChoreographyTask(ChoreographyTask task, IUpdateContext context) {
		List<Participant> participants = task.getParticipantRefs();
		int size = participants.size();
		Tuple<Text, Text> topAndBottom = getTopAndBottomTexts(context);
		
		ContainerShape container = (ContainerShape) context.getPictogramElement();
		
		int h = 20;
		int w = container.getGraphicsAlgorithm().getWidth();
		int topY = AddChoreographyTaskFeature.PARTICIPANT_BAND_HEIGHT - 5;
		int bottomY = container.getGraphicsAlgorithm().getHeight() - topY - h;
		int location = 0;
		
		for (int i = 0; i < size; i++) {
			if(i == 0) {
				topAndBottom.getFirst().setValue(participants.get(i).getName());
				continue;
			}
			if(i == 1) {
				topAndBottom.getSecond().setValue(participants.get(i).getName());
				continue;
			}
			
			Shape shape = peService.createShape(container, false);
			peService.setPropertyValue(shape, PARTICIPANT_REF, Boolean.toString(true));
			Rectangle rect = gaService.createRectangle(shape);
			
			if(location == 0) {
				gaService.setLocationAndSize(rect, 0, topY, w, h);
				topY += h - 1;
				location += 1;
			} else {
				gaService.setLocationAndSize(rect, 0, bottomY, w, h);
				bottomY -= h - 1;
				location -= 1;
			}
			
			rect.setFilled(true);
			rect.setBackground(manageColor(IColorConstant.LIGHT_GRAY));
			rect.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
			
			Text text = gaService.createText(rect);
			gaService.setLocationAndSize(text, 0, 0, w, h);
			text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
			text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
			text.setStyle(StyleUtil.getStyleForText(getDiagram()));
			text.setValue(participants.get(i).getName());
		}
		
		if(size == 0) {
			topAndBottom.getFirst().setValue("Participant A");
			topAndBottom.getSecond().setValue("Participant B");
		}
		
		if(size == 1) {
			topAndBottom.getSecond().setValue("Participant B");
		}
	}

	private Tuple<Text, Text> getTopAndBottomTexts(IUpdateContext context) {
		Text top = null;
		Text bottom = null;
		Iterator<Shape> iterator = peService.getAllContainedShapes((ContainerShape) context.getPictogramElement())
		        .iterator();
		while (iterator.hasNext()) {
			Shape shape = (Shape) iterator.next();
			String property = peService.getPropertyValue(shape, CHOREOGRAPHY_TASK_PROPERTY);
			if (property == null) {
				continue;
			}
			if (property.equals(TOP_BAND_TEXT)) {
				top = (Text) shape.getGraphicsAlgorithm();
			} else if (property.equals(BOTTOM_BAND_TEXT)) {
				bottom = (Text) shape.getGraphicsAlgorithm();
			}
			if(top != null && bottom != null) {
				break;
			}
		}
		return new Tuple<Text, Text>(top, bottom);
	}
}