package org.jboss.bpmn2.editor.core.features.data;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataObject;
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
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.styles.AdaptedGradientColoredAreas;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.PredefinedColoredAreas;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractCreateFlowElementFeature;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class DataFeatureContainer implements FeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof DataObject;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateDataObjectFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AbstractAddShapeFeature(fp) {

			protected FeatureSupport support = new FeatureSupport() {
				@Override
				public Object getBusinessObject(PictogramElement element) {
					return getBusinessObjectForPictogramElement(element);
				}
			};

			@Override
			public boolean canAdd(IAddContext context) {
				boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
				boolean intoLane = support.isTargetLane(context) && support.isTargetLaneOnTop(context);
				boolean intoParticipant = support.isTargetParticipant(context);
				boolean intoSubProcess = support.isTargetSubProcess(context);
				return intoDiagram || intoLane || intoParticipant || intoSubProcess;
			}

			@Override
			public PictogramElement add(IAddContext context) {
				IGaService gaService = Graphiti.getGaService();
				IPeService peService = Graphiti.getPeService();
				DataObject data = (DataObject) context.getNewObject();

				int width = 35;
				int height = 50;
				int e = 10;
				
				ContainerShape container = peService.createContainerShape(context.getTargetContainer(), true);
				Rectangle invisibleRect = gaService.createInvisibleRectangle(container);
				gaService.setLocationAndSize(invisibleRect, context.getX(), context.getY(), width, height);

				Shape rectShape = peService.createShape(container, false);
				Polygon rect = gaService.createPolygon(rectShape, new int[] { 0, 0, width - e, 0, width, e, width,
				        height, 0, height });
				rect.setLineWidth(1);
				rect.setStyle(StyleUtil.getStyleForClass(getDiagram()));
				AdaptedGradientColoredAreas gradient = PredefinedColoredAreas.getBlueWhiteAdaptions();
				gaService.setRenderingStyle(rect, gradient);

				int p = width - e - 1;
				Polyline edge = gaService.createPolyline(rect, new int[] { p, 0, p, e + 1, width, e + 1});
				edge.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				edge.setLineWidth(1);

				ChopboxAnchor anchor = peService.createChopboxAnchor(container);
				anchor.setReferencedGraphicsAlgorithm(rect);

				if (data.eResource() == null) {
					getDiagram().eResource().getContents().add(data);
				}

				link(container, data);
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

	public static class CreateDataObjectFeature extends AbstractCreateFlowElementFeature<DataObject> {

		public CreateDataObjectFeature(IFeatureProvider fp) {
			super(fp, "Data Object",
			        "Provides information about what activities require to be performed or what they produce");
		}

		@Override
		protected DataObject createFlowElement(ICreateContext context) {
			return ModelHandler.FACTORY.createDataObject();
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