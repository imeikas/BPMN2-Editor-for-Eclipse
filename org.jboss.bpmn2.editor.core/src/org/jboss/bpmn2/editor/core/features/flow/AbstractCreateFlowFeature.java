package org.jboss.bpmn2.editor.core.features.flow;

import java.io.IOException;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.impl.AbstractCreateConnectionFeature;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public abstract class AbstractCreateFlowFeature<T extends EObject> extends AbstractCreateConnectionFeature {

	public AbstractCreateFlowFeature(IFeatureProvider fp, String name, String description) {
	    super(fp, name, description);
    }

	@Override
    public boolean canCreate(ICreateConnectionContext context) {
		T source = getFlowNode(context.getSourceAnchor());
		T target = getFlowNode(context.getTargetAnchor());
		return source != null && target != null;
    }

	@Override
    public Connection create(ICreateConnectionContext context) {
		try {
			T source = getFlowNode(context.getSourceAnchor());
			T target = getFlowNode(context.getTargetAnchor());
			ModelHandler mh = FeatureSupport.getModelHanderInstance(getDiagram());
			AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(), context.getTargetAnchor());
			BaseElement flow = createFlow(mh, source, target);
			flow.setId(EcoreUtil.generateUUID());
			addContext.setNewObject(flow);
			return (Connection) getFeatureProvider().addIfPossible(addContext);
		} catch (IOException e) {
			Activator.logError(e);
		}
	    return null;
    }
	
	@Override
	public boolean canStartConnection(ICreateConnectionContext context) {
		return getFlowNode(context.getSourceAnchor()) != null;
	}
	
	@Override
	public String getCreateImageId() {
		return getStencilImageId();
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getStencilImageId();
	}
	
	abstract String getStencilImageId();
	
	abstract BaseElement createFlow(ModelHandler mh, T source, T target);
	
	abstract T getFlowNode(Anchor anchor);
}