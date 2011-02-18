package org.jboss.bpmn2.editor.core.features.event.definitions;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.bpmn2.LinkEventDefinition;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class LinkEventDefinitionContainer extends EventDefinitionFeatureContainer {

	@Override
    public boolean canApplyTo(BaseElement element) {
	    return element instanceof LinkEventDefinition;
    }

	@Override
    public ICreateFeature getCreateFeature(IFeatureProvider fp) {
	    return new CreateLinkEventDefinition(fp);
    }

	@Override
    protected Shape drawForStart(DecorationAlgorithm algorithm, ContainerShape shape) {
	    return null; // NOT ALLOWED ACCORDING TO SPEC
    }

	@Override
    protected Shape drawForEnd(DecorationAlgorithm algorithm, ContainerShape shape) {
	    return null; // NOT ALLOWED ACCORDING TO SPEC
    }

	@Override
    protected Shape drawForThrow(DecorationAlgorithm algorithm, ContainerShape shape) {
	    return drawFilled(algorithm, shape);
    }

	@Override
    protected Shape drawForCatch(DecorationAlgorithm algorithm, ContainerShape shape) {
	    return draw(algorithm, shape);
    }
	
	private Shape draw(DecorationAlgorithm algorithm, ContainerShape shape) {
		Shape linkShape = Graphiti.getPeService().createShape(shape, false);
		Polygon link = ShapeUtil.createEventLink(linkShape);
		link.setFilled(false);
		link.setForeground(algorithm.manageColor(StyleUtil.CLASS_FOREGROUND));
		return linkShape;
	}
	
	private Shape drawFilled(DecorationAlgorithm algorithm, ContainerShape shape) {
		Shape linkShape = Graphiti.getPeService().createShape(shape, false);
		Polygon link = ShapeUtil.createEventLink(linkShape);
		link.setFilled(true);
		link.setBackground(algorithm.manageColor(StyleUtil.CLASS_FOREGROUND));
		link.setForeground(algorithm.manageColor(StyleUtil.CLASS_FOREGROUND));
		return linkShape;
	}
	
	public static class CreateLinkEventDefinition extends CreateEventDefinition {

		public CreateLinkEventDefinition(IFeatureProvider fp) {
			super(fp, "Link Definition", "Adds link trigger to event");
		}

		@Override
		public boolean canCreate(ICreateContext context) {
			if (!super.canCreate(context))
				return false;

			Event e = (Event) getBusinessObjectForPictogramElement(context.getTargetContainer());
			
			if (e instanceof IntermediateCatchEvent || e instanceof IntermediateThrowEvent)
				return true;

			return false;
		}

		@Override
		protected EventDefinition createEventDefinition(ICreateContext context) {
			return ModelHandler.FACTORY.createLinkEventDefinition();
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_LINK;
		}
	}
}