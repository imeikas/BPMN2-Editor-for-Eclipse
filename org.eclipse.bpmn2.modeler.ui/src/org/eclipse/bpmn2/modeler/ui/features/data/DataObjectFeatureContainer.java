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
package org.eclipse.bpmn2.modeler.ui.features.data;

import java.util.Iterator;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.AbstractCreateFlowElementFeature;
import org.eclipse.bpmn2.modeler.core.features.MultiUpdateFeature;
import org.eclipse.bpmn2.modeler.core.features.UpdateBaseElementNameFeature;
import org.eclipse.bpmn2.modeler.core.features.data.AddDataFeature;
import org.eclipse.bpmn2.modeler.core.features.data.Properties;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;

public class DataObjectFeatureContainer extends AbstractDataFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof DataObject;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateDataObjectFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AddDataFeature<DataObject>(fp) {

			@Override
			public String getName(DataObject t) {
				return t.getName();
			}

		};
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature multiUpdate = new MultiUpdateFeature(fp);
		multiUpdate.addUpdateFeature(new UpdateMarkersFeature(fp));
		multiUpdate.addUpdateFeature(new UpdateBaseElementNameFeature(fp));
		return multiUpdate;
	}

	private class UpdateMarkersFeature extends AbstractUpdateFeature {

		public UpdateMarkersFeature(IFeatureProvider fp) {
			super(fp);
		}

		@Override
		public boolean canUpdate(IUpdateContext context) {
			Object o = getBusinessObjectForPictogramElement(context.getPictogramElement());
			return o != null && o instanceof BaseElement && canApplyTo(o);
		}

		@Override
		public IReason updateNeeded(IUpdateContext context) {
			IPeService peService = Graphiti.getPeService();
			ContainerShape container = (ContainerShape) context.getPictogramElement();
			DataObject data = (DataObject) getBusinessObjectForPictogramElement(container);
			boolean isCollection = Boolean.parseBoolean(peService.getPropertyValue(container,
					Properties.COLLECTION_PROPERTY));
			return data.isIsCollection() != isCollection ? Reason.createTrueReason() : Reason.createFalseReason();
		}

		@Override
		public boolean update(IUpdateContext context) {
			IPeService peService = Graphiti.getPeService();
			ContainerShape container = (ContainerShape) context.getPictogramElement();
			DataObject data = (DataObject) getBusinessObjectForPictogramElement(container);

			boolean drawCollectionMarker = data.isIsCollection();

			Iterator<Shape> iterator = peService.getAllContainedShapes(container).iterator();
			while (iterator.hasNext()) {
				Shape shape = iterator.next();
				String prop = peService.getPropertyValue(shape, Properties.HIDEABLE_PROPERTY);
				if (prop != null && new Boolean(prop)) {
					Polyline line = (Polyline) shape.getGraphicsAlgorithm();
					line.setLineVisible(drawCollectionMarker);
				}
			}

			peService.setPropertyValue(container, Properties.COLLECTION_PROPERTY,
					Boolean.toString(data.isIsCollection()));
			return true;
		}
	}

	public static class CreateDataObjectFeature extends AbstractCreateFlowElementFeature<DataObject> {

		public CreateDataObjectFeature(IFeatureProvider fp) {
			super(fp, "Data Object",
					"Provides information about what activities require to be performed or what they produce");
		}

		@Override
		protected DataObject createFlowElement(ICreateContext context) {
			DataObject data = ModelHandler.FACTORY.createDataObject();
			data.setIsCollection(false);
			data.setName("Data Object");
			return data;
		}

		@Override
		public String getCreateImageId() {
			return ImageProvider.IMG_16_DATA_OBJECT;
		}

		@Override
		public String getCreateLargeImageId() {
			return getCreateImageId();
		}
	}
}