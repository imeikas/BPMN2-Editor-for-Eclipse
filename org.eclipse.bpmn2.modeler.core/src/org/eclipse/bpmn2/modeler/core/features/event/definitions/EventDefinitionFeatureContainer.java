/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.features.event.definitions;

import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.modeler.core.features.BaseElementFeatureContainer;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
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

public abstract class EventDefinitionFeatureContainer extends BaseElementFeatureContainer {

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
				public Shape draw(ContainerShape shape) {
					if (event instanceof BoundaryEvent) {
						return drawForBoundary(this, shape);
					}
					if (event instanceof IntermediateCatchEvent) {
						return drawForCatch(this, shape);
					}
					if (event instanceof IntermediateThrowEvent) {
						return drawForThrow(this, shape);
					}
					if (event instanceof StartEvent) {
						return drawForStart(this, shape);
					}
					if (event instanceof EndEvent) {
						return drawForEnd(this, shape);
					}
					return null;
				}

				@Override
				public Color manageColor(IColorConstant colorConstant) {
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

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider context) {
		return null;
	}
}