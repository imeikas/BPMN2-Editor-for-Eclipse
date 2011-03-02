package org.jboss.bpmn2.editor.core.features.data;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Message;
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
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.styles.AdaptedGradientColoredAreas;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.PredefinedColoredAreas;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.AbstractBpmnAddFeature;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.ShapeUtil.Envelope;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class MessageFeatureContainer implements FeatureContainer {

	@Override
    public boolean canApplyTo(BaseElement element) {
	    return element instanceof Message;
    }

	@Override
    public ICreateFeature getCreateFeature(IFeatureProvider fp) {
	    return new CreateMessageFeature(fp);
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
				Message msg = (Message) context.getNewObject();
				
				int width = context.getWidth() > 0 ? context.getWidth() : 30;
				int height = context.getHeight() > 0 ? context.getHeight() : 20;
				
				ContainerShape container = peService.createContainerShape(context.getTargetContainer(), true);
				Rectangle invisibleRect = gaService.createInvisibleRectangle(container);
				gaService.setLocationAndSize(invisibleRect, context.getX(), context.getY(), width, height);
				
				Envelope envelope = ShapeUtil.createEnvelope(invisibleRect, 0, 0, width, height);
				envelope.rect.setFilled(true);
				envelope.rect.setStyle(StyleUtil.getStyleForClass(getDiagram()));
				AdaptedGradientColoredAreas gradient = PredefinedColoredAreas.getBlueWhiteAdaptions();
				gaService.setRenderingStyle(envelope.rect, gradient);
				envelope.line.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				
				ChopboxAnchor anchor = peService.createChopboxAnchor(container);
				anchor.setReferencedGraphicsAlgorithm(invisibleRect);
				
				if (msg.eResource() == null) {
					getDiagram().eResource().getContents().add(msg);
				}

				link(container, msg);
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
	
	public static class CreateMessageFeature extends AbstractCreateRootElementFeature {

		public CreateMessageFeature(IFeatureProvider fp) {
	        super(fp, "Message", "Represents the content of a communication between two Participants");
        }

		@Override
        RootElement createRootElement() {
			Message message = ModelHandler.FACTORY.createMessage();
			message.setName("Message");
	        return message;
        }

		@Override
        String getStencilImageId() {
	        return ImageProvider.IMG_16_MESSAGE;
        }
	}
}