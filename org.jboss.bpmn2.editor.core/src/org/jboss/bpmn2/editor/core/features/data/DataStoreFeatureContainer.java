package org.jboss.bpmn2.editor.core.features.data;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataStore;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.styles.AdaptedGradientColoredAreas;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.PredefinedColoredAreas;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractBpmnAddFeature;
import org.jboss.bpmn2.editor.core.features.DefaultBpmnMoveFeature;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;
import org.jboss.bpmn2.editor.utils.AnchorUtil;
import org.jboss.bpmn2.editor.utils.StyleUtil;

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
		return new AbstractBpmnAddFeature(fp) {

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

				peService.createChopboxAnchor(container);
				AnchorUtil.addFixedPointAnchors(container, invisibleRect);

				link(container, store);
				createDIShape(container, store);
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

	public static class CreateDataStoreFeature extends AbstractCreateRootElementFeature {

		public CreateDataStoreFeature(IFeatureProvider fp) {
			super(fp, "Data Store", "Persist information that is beyond the scope of the process");
		}

		@Override
        RootElement createRootElement() {
			DataStore dataStore = ModelHandler.FACTORY.createDataStore();
			dataStore.setName("Data Store");
	        return dataStore;
        }

		@Override
        String getStencilImageId() {
	        return ImageProvider.IMG_16_DATA_STORE;
        }
	}
}