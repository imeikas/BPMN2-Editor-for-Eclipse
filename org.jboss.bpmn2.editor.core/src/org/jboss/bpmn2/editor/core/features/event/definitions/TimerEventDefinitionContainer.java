package org.jboss.bpmn2.editor.core.features.event.definitions;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.ThrowEvent;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;

public class TimerEventDefinitionContainer extends EventDefinitionFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof TimerEventDefinition;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateTimerEventDefinition(fp);
	}

	@Override
	protected Shape drawForStart(DecorationAlgorithm algorithm, ContainerShape shape) {
		return draw(shape);
	}

	@Override
	protected Shape drawForEnd(DecorationAlgorithm algorithm, ContainerShape shape) {
		return draw(shape);
	}

	@Override
	protected Shape drawForThrow(DecorationAlgorithm decorationAlgorithm, ContainerShape shape) {
		return null; // NOT ALLOWED ACCORDING TO SPEC
	}

	@Override
	protected Shape drawForCatch(DecorationAlgorithm decorationAlgorithm, ContainerShape shape) {
		return draw(shape);
	}

	@Override
	protected Shape drawForBoundary(DecorationAlgorithm algorithm, ContainerShape shape) {
		return draw(shape);
	}

	private Shape draw(ContainerShape shape) {
		Shape timerShape = Graphiti.getPeService().createShape(shape, false);
		ShapeUtil.createEventImage(timerShape, ImageProvider.IMG_20_TIMER);
		return timerShape;
	}

	public static class CreateTimerEventDefinition extends CreateEventDefinition {

		public CreateTimerEventDefinition(IFeatureProvider fp) {
			super(fp, "Timer Definition", "Adds time condition to event");
		}

		@Override
		public boolean canCreate(ICreateContext context) {
			if (!super.canCreate(context)) {
				return false;
			}

			Event e = (Event) BusinessObjectUtil.getFirstElementOfType(context.getTargetContainer(), Event.class);
			if (e instanceof ThrowEvent) {
				return false;
			}

			return true;
		}

		@Override
		protected EventDefinition createEventDefinition(ICreateContext context) {
			return ModelHandler.FACTORY.createTimerEventDefinition();
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_TIMER;
		}
	}
}