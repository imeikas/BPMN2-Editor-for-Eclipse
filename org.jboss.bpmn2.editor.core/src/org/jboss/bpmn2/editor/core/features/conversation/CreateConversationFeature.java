package org.jboss.bpmn2.editor.core.features.conversation;

import java.io.IOException;

import org.eclipse.bpmn2.Conversation;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public class CreateConversationFeature extends AbstractCreateFeature {

	public CreateConversationFeature(IFeatureProvider fp) {
		super(fp, "Conversation", "A logical grouping of Message exchanges");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		return context.getTargetContainer().equals(getDiagram());
	}

	@Override
	public Object[] create(ICreateContext context) {
		Conversation c = null;
		try {
			ModelHandler handler = FeatureSupport.getModelHanderInstance(getDiagram());
			c = ModelHandler.FACTORY.createConversation();
			c.setId(EcoreUtil.generateUUID());
			//TODO add to model
		} catch (IOException e) {
			Activator.logError(e);
		}
		addGraphicalRepresentation(context, c);
		return new Object[] { c };
	}
	
	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_CONVERSATION;
	}
	
	@Override
	public String getCreateLargeImageId() {
		return ImageProvider.IMG_16_CONVERSATION;
	}
}