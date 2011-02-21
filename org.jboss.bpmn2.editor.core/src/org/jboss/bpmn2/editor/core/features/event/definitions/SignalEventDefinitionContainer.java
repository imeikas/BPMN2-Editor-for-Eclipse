package org.jboss.bpmn2.editor.core.features.event.definitions;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.SignalEventDefinition;
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

public class SignalEventDefinitionContainer extends EventDefinitionFeatureContainer {

	@Override
    public boolean canApplyTo(BaseElement element) {
	    return element instanceof SignalEventDefinition;
    }

	@Override
    public ICreateFeature getCreateFeature(IFeatureProvider fp) {
	    return new CreateSignalEventDefinition(fp);
    }

	@Override
    protected Shape drawForStart(DecorationAlgorithm algorithm, ContainerShape shape) {
		return drawSignal(algorithm, shape);
    }

	@Override
    protected Shape drawForEnd(DecorationAlgorithm algorithm, ContainerShape shape) {
		return drawSignalFilled(algorithm, shape);
    }
	
	@Override
    protected Shape drawForThrow(DecorationAlgorithm algorithm, ContainerShape shape) {
	    return drawSignalFilled(algorithm, shape);
    }

	@Override
    protected Shape drawForCatch(DecorationAlgorithm algorithm, ContainerShape shape) {
	    return drawSignal(algorithm, shape);
    }
	
	@Override
    protected Shape drawForBoundary(DecorationAlgorithm algorithm, ContainerShape shape) {
	    // TODO Auto-generated method stub
	    return null;
    }
	
	private Shape drawSignal(DecorationAlgorithm algorithm, ContainerShape shape) {
		Shape signalShape = Graphiti.getPeService().createShape(shape, false);
		Polygon signal = ShapeUtil.createEventSignal(signalShape);
		signal.setFilled(false);
		signal.setForeground(algorithm.manageColor(StyleUtil.CLASS_FOREGROUND));
	    return signalShape;
	}
	
	private Shape drawSignalFilled(DecorationAlgorithm algorithm, ContainerShape shape) {
		Shape signalShape = Graphiti.getPeService().createShape(shape, false);
		Polygon signal = ShapeUtil.createEventSignal(signalShape);
		signal.setFilled(true);
		signal.setBackground(algorithm.manageColor(StyleUtil.CLASS_FOREGROUND));
		signal.setForeground(algorithm.manageColor(StyleUtil.CLASS_FOREGROUND));
	    return signalShape;
	}
	
	public static class CreateSignalEventDefinition extends CreateEventDefinition {

		public CreateSignalEventDefinition(IFeatureProvider fp) {
	        super(fp, "Signal Definition", "Adds signal definition to event");
        }

		@Override
        protected EventDefinition createEventDefinition(ICreateContext context) {
	        return ModelHandler.FACTORY.createSignalEventDefinition();
        }

		@Override
        protected String getStencilImageId() {
	        return ImageProvider.IMG_16_SIGNAL;
        }
	}
}
