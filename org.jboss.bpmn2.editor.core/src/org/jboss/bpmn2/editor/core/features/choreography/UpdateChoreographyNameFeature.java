package org.jboss.bpmn2.editor.core.features.choreography;

import static org.jboss.bpmn2.editor.core.features.choreography.Properties.*;
import java.util.Iterator;

import org.eclipse.bpmn2.ChoreographyTask;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class UpdateChoreographyNameFeature extends AbstractUpdateFeature {

	private static final IPeService peService = Graphiti.getPeService();

	public UpdateChoreographyNameFeature(IFeatureProvider fp) {
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
		if (task.getName().equals(getBodyText(context).getValue())) {
			return Reason.createFalseReason();
		} else {
			return Reason.createTrueReason("Name is out of date");
		}
	}

	@Override
	public boolean update(IUpdateContext context) {
		ChoreographyTask task = (ChoreographyTask) BusinessObjectUtil.getFirstElementOfType(
		        context.getPictogramElement(), ChoreographyTask.class);
		getBodyText(context).setValue(task.getName());
		return true;
	}

	private Text getBodyText(IUpdateContext context) {
		Iterator<Shape> iterator = peService.getAllContainedShapes((ContainerShape) context.getPictogramElement())
		        .iterator();
		while (iterator.hasNext()) {
			Shape shape = (Shape) iterator.next();
			String property = peService.getPropertyValue(shape, CHOREOGRAPHY_TASK_PROPERTY);
			if (property != null && property.equals(BODY_BAND_TEXT)) {
				return (Text) shape.getGraphicsAlgorithm();
			}
		}
		return null;
	}
}