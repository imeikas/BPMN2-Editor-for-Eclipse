package org.jboss.bpmn2.editor.core.features.event.definitions;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.CancelEventDefinition;
import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.utils.ShapeUtil;
import org.jboss.bpmn2.editor.utils.StyleUtil;

public class CancelEventDefinitionContainer extends EventDefinitionFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof CancelEventDefinition;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateCancelEventDefinition(fp);
	}

	@Override
	protected Shape drawForStart(DecorationAlgorithm algorithm, ContainerShape shape) {
		return null; // NOT ALLOWED ACCORDING TO SPEC
	}

	@Override
	protected Shape drawForEnd(DecorationAlgorithm algorithm, ContainerShape shape) {
		return drawFilled(algorithm, shape);
	}

	@Override
	protected Shape drawForThrow(DecorationAlgorithm algorithm, ContainerShape shape) {
		return null; // NOT ALLOWED ACCORDING TO SPEC
	}

	@Override
	protected Shape drawForCatch(DecorationAlgorithm algorithm, ContainerShape shape) {
		return null; // NOT ALLOWED ACCORDING TO SPEC
	}

	@Override
	protected Shape drawForBoundary(DecorationAlgorithm algorithm, ContainerShape shape) {
		return draw(algorithm, shape);
	}

	private Shape draw(DecorationAlgorithm algorithm, ContainerShape shape) {
		Shape cancelShape = Graphiti.getPeService().createShape(shape, false);
		Polygon link = ShapeUtil.createEventCancel(cancelShape);
		link.setFilled(false);
		link.setForeground(algorithm.manageColor(StyleUtil.CLASS_FOREGROUND));
		return cancelShape;
	}

	private Shape drawFilled(DecorationAlgorithm algorithm, ContainerShape shape) {
		Shape cancelShape = Graphiti.getPeService().createShape(shape, false);
		Polygon link = ShapeUtil.createEventCancel(cancelShape);
		link.setFilled(true);
		link.setBackground(algorithm.manageColor(StyleUtil.CLASS_FOREGROUND));
		link.setForeground(algorithm.manageColor(StyleUtil.CLASS_FOREGROUND));
		return cancelShape;
	}

	public static class CreateCancelEventDefinition extends CreateEventDefinition {

		public CreateCancelEventDefinition(IFeatureProvider fp) {
			super(fp, "Cancel Definition", "Adds cancel trigger to event");
		}

		@Override
		public boolean canCreate(ICreateContext context) {
			if (!super.canCreate(context)) {
				return false;
			}

			Event e = (Event) getBusinessObjectForPictogramElement(context.getTargetContainer());

			if (e instanceof BoundaryEvent) {
				BoundaryEvent be = (BoundaryEvent) e;
				return be.isCancelActivity();
			}

			if (e instanceof CatchEvent || e instanceof IntermediateThrowEvent) {
				return false;
			}

			return true;
		}

		@Override
		protected EventDefinition createEventDefinition(ICreateContext context) {
			CancelEventDefinition definition = ModelHandler.FACTORY.createCancelEventDefinition();
			definition.setId(EcoreUtil.generateUUID());
			return definition;
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_CANCEL;
		}
	}
}
