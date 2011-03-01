package org.jboss.bpmn2.editor.core.features.data;

import java.io.IOException;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public abstract class AbstractCreateDataInputOutputFeature extends AbstractCreateFeature {

	public AbstractCreateDataInputOutputFeature(IFeatureProvider fp, String name, String description) {
		super(fp, name, description);
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		return true;
	}

	@Override
	public Object[] create(ICreateContext context) {
		BaseElement element = null;
		try {
			ModelHandler handler = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
			element = add(BusinessObjectUtil.getFirstElementOfType(context.getTargetContainer(), BaseElement.class),
					handler);
		} catch (IOException e) {
			Activator.logError(e);
		}
		addGraphicalRepresentation(context, element);
		return new Object[] { element };
	}

	abstract <T extends BaseElement> T add(Object target, ModelHandler handler);

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