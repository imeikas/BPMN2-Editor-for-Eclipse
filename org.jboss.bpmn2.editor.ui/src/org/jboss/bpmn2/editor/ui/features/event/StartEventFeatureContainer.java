package org.jboss.bpmn2.editor.ui.features.event;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.MultiUpdateFeature;
import org.jboss.bpmn2.editor.core.features.event.AbstractCreateEventFeature;
import org.jboss.bpmn2.editor.core.features.event.AbstractEventFeatureContainer;
import org.jboss.bpmn2.editor.core.features.event.AddEventFeature;
import org.jboss.bpmn2.editor.ui.ImageProvider;

public class StartEventFeatureContainer extends AbstractEventFeatureContainer {

	static final String INTERRUPTING = "interrupting";

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof StartEvent;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateStartEventFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddEventFeature(fp) {
			@Override
			protected void hook(ContainerShape container) {
				Graphiti.getPeService().setPropertyValue(container, INTERRUPTING, Boolean.toString(true));
			}
		};
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		IUpdateFeature defaultUpdateFeature = super.getUpdateFeature(fp);
		MultiUpdateFeature updateFeature = new MultiUpdateFeature(fp);
		updateFeature.addUpdateFeature(defaultUpdateFeature);
		updateFeature.addUpdateFeature(new SubProcessEventUpdateFeature(fp));
		return updateFeature;
	}

	public static class CreateStartEventFeature extends AbstractCreateEventFeature {

		public CreateStartEventFeature(IFeatureProvider fp) {
			super(fp, "Start Event", "Indicates the start of a process or choreography");
		}

		@Override
		protected Event createFlowElement(ICreateContext context) {
			StartEvent start = ModelHandler.FACTORY.createStartEvent();
			start.setName("Start");
			start.setIsInterrupting(true);
			return start;
		}

		@Override
		public String getStencilImageId() {
			return ImageProvider.IMG_16_START_EVENT;
		}
	}

	private class SubProcessEventUpdateFeature extends AbstractUpdateFeature {

		public SubProcessEventUpdateFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public boolean canUpdate(IUpdateContext context) {
			Object o = getBusinessObjectForPictogramElement(context.getPictogramElement());
			return o != null && o instanceof StartEvent;
		}

		@Override
		public IReason updateNeeded(IUpdateContext context) {
			IPeService peService = Graphiti.getPeService();
			PictogramElement element = context.getPictogramElement();
			
			String prop = peService.getPropertyValue(element, INTERRUPTING);
			if(prop == null) {
				return Reason.createFalseReason();
			}
			
			StartEvent event = (StartEvent) getBusinessObjectForPictogramElement(element);
			boolean interrupting = Boolean.parseBoolean(prop);
			IReason reason = event.isIsInterrupting() == interrupting ? Reason.createFalseReason() : Reason.createTrueReason();
			return reason;
		}

		@Override
		public boolean update(IUpdateContext context) {
			IPeService peService = Graphiti.getPeService();
			ContainerShape container = (ContainerShape) context.getPictogramElement();
			StartEvent event = (StartEvent) getBusinessObjectForPictogramElement(container);

			Ellipse ellipse = (Ellipse) peService.getAllContainedShapes(container).iterator().next()
			        .getGraphicsAlgorithm();
			LineStyle style = event.isIsInterrupting() ? LineStyle.SOLID : LineStyle.DASH;
			ellipse.setLineStyle(style);

			peService.setPropertyValue(container, INTERRUPTING, Boolean.toString(event.isIsInterrupting()));
			return true;
		}
	}
}