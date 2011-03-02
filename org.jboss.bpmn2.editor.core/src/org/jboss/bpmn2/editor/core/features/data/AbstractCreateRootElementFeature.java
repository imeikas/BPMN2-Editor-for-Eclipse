package org.jboss.bpmn2.editor.core.features.data;

import java.io.IOException;

import org.eclipse.bpmn2.RootElement;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;

public abstract class AbstractCreateRootElementFeature extends AbstractCreateFeature {

	public AbstractCreateRootElementFeature(IFeatureProvider fp, String name, String description) {
	    super(fp, name, description);
    }

	@Override
    public boolean canCreate(ICreateContext context) {
	    return true;
    }

	@Override
    public Object[] create(ICreateContext context) {
		RootElement element = null;
		
		try {
			ModelHandler handler = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
			element = createRootElement();
			element.setId(EcoreUtil.generateUUID());
			handler.addRootElement(element);
		} catch (IOException e) {
			Activator.logError(e);
		}

		addGraphicalRepresentation(context, element);
		return new Object[] { element };
    }
	
	abstract RootElement createRootElement();
	
	abstract String getStencilImageId();
	
	@Override
	public String getCreateImageId() {
	    return getStencilImageId();
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getStencilImageId();
	}
}
