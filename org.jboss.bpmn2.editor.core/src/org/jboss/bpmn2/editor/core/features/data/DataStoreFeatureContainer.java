package org.jboss.bpmn2.editor.core.features.data;

import java.io.IOException;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataStore;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.styles.AdaptedGradientColoredAreas;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.PredefinedColoredAreas;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class DataStoreFeatureContainer implements FeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof DataStore;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateDataStoreFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AbstractAddShapeFeature(fp) {

			@Override
			public boolean canAdd(IAddContext context) {
				return true;
			}

			@Override
			public PictogramElement add(IAddContext context) {
				IGaService gaService = Graphiti.getGaService();
				IPeService peService = Graphiti.getPeService();
				DataStore store = (DataStore) context.getNewObject();

				int width = 50;
				int height = 50;

				ContainerShape container = peService.createContainerShape(context.getTargetContainer(), true);
				Rectangle invisibleRect = gaService.createInvisibleRectangle(container);
				gaService.setLocationAndSize(invisibleRect, context.getX(), context.getY(), width, height);

				int whalf = width / 2;
				
				int[] xy = { 0, 10, whalf, 20, width, 10, width, height - 10, whalf, height, 0, height - 10 };
				int[] bend = { 0, 0, whalf, whalf, 0, 0, 0, 0, whalf, whalf, 0, 0 };
				Polygon polygon = gaService.createPolygon(invisibleRect, xy, bend);
				polygon.setFilled(true);
				polygon.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				AdaptedGradientColoredAreas gradient = PredefinedColoredAreas.getBlueWhiteAdaptions();
				gaService.setRenderingStyle(polygon, gradient);
				
				xy = new int[] {0, 14, whalf, 24, width, 14};
				bend = new int[] {0, 0, whalf, whalf, 0, 0};
				Polyline line1 = gaService.createPolyline(invisibleRect, xy, bend);
				line1.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				
				xy = new int[] {0, 18, whalf, 28, width, 18};
				Polyline line2 = gaService.createPolyline(invisibleRect, xy, bend);
				line2.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				
				xy = new int[] {0, 11, whalf, 0, width, 11};
				Polyline lineTop = gaService.createPolyline(invisibleRect, xy, bend);
				lineTop.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				
				ChopboxAnchor anchor = peService.createChopboxAnchor(container);
				anchor.setReferencedGraphicsAlgorithm(invisibleRect);

				if (store.eResource() == null) {
					getDiagram().eResource().getContents().add(store);
				}

				link(container, store);
				return container;
			}
		};
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		return null;
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
		return new DefaultResizeShapeFeature(fp) {
			@Override
			public boolean canResizeShape(IResizeShapeContext context) {
				return false;
			}
		};
	}

	public static class CreateDataStoreFeature extends AbstractCreateFeature {

		public CreateDataStoreFeature(IFeatureProvider fp) {
			super(fp, "Data Store", "Persist information that is beyond the scope of the process");
		}

		@Override
		public boolean canCreate(ICreateContext context) {
			return true;
		}

		@Override
		public Object[] create(ICreateContext context) {
			DataStore store = null;
			try {
				ModelHandler handler = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
				store = handler.addRootElement(ModelHandler.FACTORY.createDataStore());
				store.setName("Data Store");
			} catch (IOException e) {
				Activator.logError(e);
			}

			addGraphicalRepresentation(context, store);
			return new Object[] { store };
		}

		@Override
		public String getCreateImageId() {
			return ImageProvider.IMG_16_DATA_STORE;
		}

		@Override
		public String getCreateLargeImageId() {
			return getCreateImageId();
		}
	}
}