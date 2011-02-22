package org.jboss.bpmn2.editor.core.features.event.definitions;

import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.util.IColorConstant;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;

public abstract class EventDefinitionFeatureContainer implements FeatureContainer {

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddEventDefinitionFeature(fp);
	}
	
	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
	    return null;
	}
	
	protected abstract Shape drawForStart(DecorationAlgorithm algorithm, ContainerShape shape);

	protected abstract Shape drawForEnd(DecorationAlgorithm algorithm, ContainerShape shape);
	
	protected abstract Shape drawForThrow(DecorationAlgorithm algorithm, ContainerShape shape);

	protected abstract Shape drawForCatch(DecorationAlgorithm algorithm, ContainerShape shape);
	
	protected abstract Shape drawForBoundary(DecorationAlgorithm algorithm, ContainerShape shape);
	
	class AddEventDefinitionFeature extends AbstractAddEventDefinitionFeature {

		public AddEventDefinitionFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		protected DecorationAlgorithm getDecorationAlgorithm(final Event event) {
			return new DecorationAlgorithm() {

				@Override
				Shape draw(ContainerShape shape) {
					if (event instanceof BoundaryEvent)
						return drawForBoundary(this, shape);
					if (event instanceof IntermediateCatchEvent)
						return drawForCatch(this, shape);
					if (event instanceof IntermediateThrowEvent)
						return drawForThrow(this, shape);
					if (event instanceof StartEvent)
						return drawForStart(this, shape);
					if (event instanceof EndEvent)
						return drawForEnd(this, shape);
					return null;
				}

				@Override
				Color manageColor(IColorConstant colorConstant) {
					return AddEventDefinitionFeature.this.manageColor(colorConstant);
				}
			};
		}
	}
	
	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
	    return null;
	}
	
	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
	    return null;
	}
	
	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
	    return null;
	}
	
	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
	    return null;
	}
}