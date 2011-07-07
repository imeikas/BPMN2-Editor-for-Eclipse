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

import java.io.IOException;

import org.eclipse.bpmn2.DataStore;
import org.eclipse.bpmn2.DataStoreReference;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmnAddFeature;
import org.eclipse.bpmn2.modeler.core.features.AbstractCreateFlowElementFeature;
import org.eclipse.bpmn2.modeler.core.features.BaseElementFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.DefaultBpmnMoveFeature;
import org.eclipse.bpmn2.modeler.core.features.UpdateBaseElementNameFeature;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.bpmn2.modeler.ui.features.LayoutBaseElementTextFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public class DataStoreReferenceFeatureContainer extends BaseElementFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof DataStoreReference;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateDataStoreReferenceFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AbstractBpmnAddFeature(fp) {

			@Override
			public boolean canAdd(IAddContext context) {
				return true;
			}

			@Override
			public PictogramElement add(IAddContext context) {
				IGaService gaService = Graphiti.getGaService();
				IPeService peService = Graphiti.getPeService();
				DataStoreReference store = (DataStoreReference) context.getNewObject();

				int width = 50;
				int height = 50;
				int textArea = 15;

				ContainerShape container = peService.createContainerShape(context.getTargetContainer(), true);
				Rectangle invisibleRect = gaService.createInvisibleRectangle(container);
				gaService.setLocationAndSize(invisibleRect, context.getX(), context.getY(), width, height + textArea);

				int whalf = width / 2;

				int[] xy = { 0, 10, whalf, 20, width, 10, width, height - 10, whalf, height, 0, height - 10 };
				int[] bend = { 0, 0, whalf, whalf, 0, 0, 0, 0, whalf, whalf, 0, 0 };
				Polygon polygon = gaService.createPolygon(invisibleRect, xy, bend);
				polygon.setFilled(true);
				// polygon.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));

				StyleUtil.applyBGStyle(polygon, this);

				xy = new int[] { 0, 14, whalf, 24, width, 14 };
				bend = new int[] { 0, 0, whalf, whalf, 0, 0 };
				Polyline line1 = gaService.createPolyline(invisibleRect, xy, bend);
				line1.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));

				xy = new int[] { 0, 18, whalf, 28, width, 18 };
				Polyline line2 = gaService.createPolyline(invisibleRect, xy, bend);
				line2.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));

				xy = new int[] { 0, 11, whalf, 0, width, 11 };
				Polyline lineTop = gaService.createPolyline(invisibleRect, xy, bend);
				lineTop.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));

				Shape textShape = peService.createShape(container, false);
				peService
						.setPropertyValue(textShape, UpdateBaseElementNameFeature.TEXT_ELEMENT, Boolean.toString(true));
				Text text = gaService.createDefaultText(getDiagram(), textShape, store.getName());
				text.setStyle(StyleUtil.getStyleForText(getDiagram()));
				text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
				text.setVerticalAlignment(Orientation.ALIGNMENT_TOP);
				gaService.setLocationAndSize(text, 0, height, width, textArea);

				peService.createChopboxAnchor(container);
				AnchorUtil.addFixedPointAnchors(container, invisibleRect);
				createDIShape(container, store);
				layoutPictogramElement(container);
				return container;
			}
		};
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		return new UpdateBaseElementNameFeature(fp);
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return new LayoutBaseElementTextFeature(fp) {

			@Override
			public int getMinimumWidth() {
				return 50;
			}
		};
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
		return new DefaultBpmnMoveFeature(fp);
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
		return new DefaultResizeShapeFeature(fp) {
			@Override
			public boolean canResizeShape(IResizeShapeContext context) {
				return false;
			}
		};
	}

	public static class CreateDataStoreReferenceFeature extends AbstractCreateFlowElementFeature<DataStoreReference> {

		public CreateDataStoreReferenceFeature(IFeatureProvider fp) {
			super(fp, "Data Store", "Persist information that is beyond the scope of the process");
		}

		@Override
		protected DataStoreReference createFlowElement(ICreateContext context) {
			DataStoreReference dataStoreReference = null;
			try {
				dataStoreReference = ModelHandler.FACTORY.createDataStoreReference();
				dataStoreReference.setName("Data Store Ref");
				DataStore dataStore = ModelHandler.FACTORY.createDataStore();
				dataStore.setName("Data Store");
//				dataStore.setId(EcoreUtil.generateUUID());
				ModelHandler.getInstance(getDiagram()).addRootElement(dataStore);
				ModelUtil.setID(dataStore);
			} catch (IOException e) {
				Activator.showErrorWithLogging(e);
			}
			return dataStoreReference;
		}

		@Override
		public String getCreateImageId() {
			return ImageProvider.IMG_16_DATA_STORE;
		}

		@Override
		public String getCreateLargeImageId() {
			return ImageProvider.IMG_16_DATA_STORE;
		}
	}

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider context) {
		return null;
	}
}