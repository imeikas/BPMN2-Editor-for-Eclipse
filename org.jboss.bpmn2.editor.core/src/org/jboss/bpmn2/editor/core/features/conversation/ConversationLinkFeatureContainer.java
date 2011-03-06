package org.jboss.bpmn2.editor.core.features.conversation;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.ConversationLink;
import org.eclipse.bpmn2.InteractionNode;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.ConnectionFeatureContainer;
import org.jboss.bpmn2.editor.core.features.flow.AbstractAddFlowFeature;
import org.jboss.bpmn2.editor.core.features.flow.AbstractCreateFlowFeature;

public class ConversationLinkFeatureContainer extends ConnectionFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof ConversationLink;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AbstractAddFlowFeature(fp) {
			
			@Override
			protected void decorateConnectionLine(Polyline connectionLine) {
				connectionLine.setLineWidth(3);
			}
			
			@Override
			protected Class<? extends BaseElement> getBoClass() {
				return ConversationLink.class;
			}
		};
	}

	@Override
	public ICreateConnectionFeature getCreateConnectionFeature(IFeatureProvider fp) {
		return new CreateConversationLinkFeature(fp);
	}

	public static class CreateConversationLinkFeature extends AbstractCreateFlowFeature<InteractionNode, InteractionNode> {

		public CreateConversationLinkFeature(IFeatureProvider fp) {
			super(fp, "Conversation Link", "Connects Conversation nodes to and from Participants");
		}

		@Override
        protected String getStencilImageId() {
	        return ImageProvider.IMG_16_CONVERSATION_LINK;
        }

		@Override
        protected BaseElement createFlow(ModelHandler mh, InteractionNode source, InteractionNode target) {
			ConversationLink conversationLink = ModelHandler.FACTORY.createConversationLink();
			conversationLink.setName("Conversation Link");
			conversationLink.setSourceRef(source);
			conversationLink.setTargetRef(target);
			// TODO add to model
	        return conversationLink;
        }

		@Override
        protected Class<InteractionNode> getSourceClass() {
	        return InteractionNode.class;
        }

		@Override
        protected Class<InteractionNode> getTargetClass() {
	        return InteractionNode.class;
        }
	}
}