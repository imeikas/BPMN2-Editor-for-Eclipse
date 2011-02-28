package org.jboss.bpmn2.editor.core.features.data;

import java.util.Iterator;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataObject;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.features.impl.Reason;
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
	
	static final String COLLECTION_PROPERTY = "isCollection";
	static final String HIDEABLE_PROPERTY = "hideable";
	
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

				int width = 36;
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
				
				int whalf = width / 2;
				createCollectionShape(container, new int[] {whalf - 2, height - 8, whalf - 2, height});
				createCollectionShape(container, new int[] {whalf, height - 8, whalf, height});
				createCollectionShape(container, new int[] {whalf + 2, height - 8, whalf + 2, height});
				
				ChopboxAnchor anchor = peService.createChopboxAnchor(container);
				anchor.setReferencedGraphicsAlgorithm(rect);

				if (data.eResource() == null) {
					getDiagram().eResource().getContents().add(data);
				}
				
				Graphiti.getPeService().setPropertyValue(container, COLLECTION_PROPERTY, Boolean.toString(false));
				link(container, data);
				return container;
			}
			
			private Shape createCollectionShape(ContainerShape container, int[] xy) {
				IPeService peService = Graphiti.getPeService();
				IGaService gaService = Graphiti.getGaService();
				Shape collectionShape = peService.createShape(container, false);
				Polyline line = gaService.createPolyline(collectionShape, xy);
				line.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				line.setLineWidth(1);
				line.setLineVisible(false);
				peService.setPropertyValue(collectionShape, HIDEABLE_PROPERTY, Boolean.toString(true));
				return collectionShape;
			}
		};
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		return new AbstractUpdateFeature(fp) {

			@Override
            public boolean canUpdate(IUpdateContext context) {
				Object o = getBusinessObjectForPictogramElement(context.getPictogramElement());
	            return o != null && o instanceof BaseElement && canApplyTo((BaseElement) o);
            }

			@Override
            public IReason updateNeeded(IUpdateContext context) {
				IPeService peService = Graphiti.getPeService();
				ContainerShape container = (ContainerShape) context.getPictogramElement();
	            DataObject data = (DataObject) getBusinessObjectForPictogramElement(container);
	            boolean isCollection = Boolean.parseBoolean(peService.getPropertyValue(container, COLLECTION_PROPERTY));
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
	                Shape shape = (Shape) iterator.next();
	                String prop = peService.getPropertyValue(shape, HIDEABLE_PROPERTY);
	                if(prop != null && new Boolean(prop)) {
	                	Polyline line = (Polyline) shape.getGraphicsAlgorithm();
	                	line.setLineVisible(drawCollectionMarker);
	                }
                }
	            
	            peService.setPropertyValue(container, COLLECTION_PROPERTY, Boolean.toString(data.isIsCollection()));
	            return true;
            }
		};
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