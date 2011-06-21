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
package org.eclipse.bpmn2.modeler.ui.features.conversation;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Conversation;
import org.eclipse.bpmn2.ConversationLink;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.modeler.core.ModelHandler;
import org.eclipse.bpmn2.modeler.core.features.BaseElementConnectionFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractAddFlowFeature;
import org.eclipse.bpmn2.modeler.core.features.flow.AbstractCreateFlowFeature;
import org.eclipse.bpmn2.modeler.ui.ImageProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Polyline;

public class ConversationLinkFeatureContainer extends BaseElementConnectionFeatureContainer {

	@Override
	public boolean canApplyTo(Object o) {
		return super.canApplyTo(o) && o instanceof ConversationLink;
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

	public static class CreateConversationLinkFeature extends AbstractCreateFlowFeature<Conversation, Participant> {

		public CreateConversationLinkFeature(IFeatureProvider fp) {
			super(fp, "Conversation Link", "Connects Conversation nodes to and from Participants");
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_CONVERSATION_LINK;
		}

		@Override
		protected BaseElement createFlow(ModelHandler mh, Conversation source, Participant target) {
			ConversationLink conversationLink = mh.createConversationLink(source, target);
			conversationLink.setName("Conversation Link");
			return conversationLink;
		}

		@Override
		protected Class<Conversation> getSourceClass() {
			return Conversation.class;
		}

		@Override
		protected Class<Participant> getTargetClass() {
			return Participant.class;
		}
	}
}