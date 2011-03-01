package org.jboss.bpmn2.editor.core.features.artifact;

import java.io.IOException;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.impl.AbstractCreateConnectionFeature;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class CreateAssociationFeature extends AbstractCreateConnectionFeature {

	public CreateAssociationFeature(IFeatureProvider fp) {
		super(fp, "Association", "Associate information with artifacts and flow objects");
	}

	@Override
	public boolean canCreate(ICreateConnectionContext context) {
		BaseElement source = getBaseElement(context.getSourceAnchor());
		BaseElement target = getBaseElement(context.getTargetAnchor());
		return source != null && target != null;
	}

	@Override
	public Connection create(ICreateConnectionContext context) {
		BaseElement source = getBaseElement(context.getSourceAnchor());
		BaseElement target = getBaseElement(context.getTargetAnchor());

		if (source != null && target != null) {
			try {
				ModelHandler mh = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
				Association association = mh.createAssociation(source, target);
				AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(),
						context.getTargetAnchor());
				addContext.setNewObject(association);
				return (Connection) getFeatureProvider().addIfPossible(addContext);
			} catch (IOException e) {
				Activator.logError(e);
			}
		}
		return null;
	}

	@Override
	public boolean canStartConnection(ICreateConnectionContext context) {
		return getBaseElement(context.getSourceAnchor()) != null;
	}

	private BaseElement getBaseElement(Anchor anchor) {
		if (anchor != null) {
			Object o = BusinessObjectUtil.getFirstElementOfType(anchor.getParent(), BaseElement.class);
			if (o instanceof BaseElement) {
				return (BaseElement) o;
			}
		}
		return null;
	}

	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_ASSOCIATION;
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); // FIXME
	}
}
