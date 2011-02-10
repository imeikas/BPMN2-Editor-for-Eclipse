package org.jboss.bpmn2.editor.core.features.messageflow;

import java.io.IOException;

import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.impl.AbstractCreateConnectionFeature;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public class CreateMessageFlowFeature extends AbstractCreateConnectionFeature {
	
	private FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};
	
	public CreateMessageFlowFeature(IFeatureProvider fp) {
		super(fp, "Message Flow", "Represents message between two participants");
	}

	@Override
	public boolean canCreate(ICreateConnectionContext context) {
		InteractionNode source = getFlowNode(context.getSourceAnchor());
		InteractionNode target = getFlowNode(context.getTargetAnchor());
		if(source == null || target == null) {
			return false;
		}
		return isDifferentParticipants(source, target);
	}

	@Override
	public Connection create(ICreateConnectionContext context) {
		InteractionNode source = getFlowNode(context.getSourceAnchor());
		InteractionNode target = getFlowNode(context.getTargetAnchor());

		if (source != null || target != null) {
			try {
				ModelHandler mh = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
				MessageFlow flow = mh.createMessageFlow(source, target);

				AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(),
				        context.getTargetAnchor());
				addContext.setNewObject(flow);

				return (Connection) getFeatureProvider().addIfPossible(addContext);
			} catch (IOException e) {
				Activator.logError(e);
			}
		}
		return null;
	}

	@Override
	public boolean canStartConnection(ICreateConnectionContext context) {
		return getFlowNode(context.getSourceAnchor()) != null;
	}

	private InteractionNode getFlowNode(Anchor anchor) {
		if (anchor != null) {
			Object bo = getBusinessObjectForPictogramElement(anchor.getParent());
			if (bo instanceof InteractionNode) {
				return (InteractionNode) bo;
			}
		}
		return null;
	}
	
	@Override
	public String getCreateImageId() {
	    return ImageProvider.IMG_16_MESSAGE_FLOW;
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId();
	}
	
	private boolean isDifferentParticipants(InteractionNode source, InteractionNode target) {
		boolean different = false;
		try {
	        ModelHandler handler = support.getModelHanderInstance(getDiagram());
	        different = !handler.getParticipant(source).equals(handler.getParticipant(target));
        } catch (IOException e) {
        	Activator.logError(e);
        }
        return different;
	}
}
