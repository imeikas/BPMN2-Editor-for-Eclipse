package org.jboss.bpmn2.editor.core.features;

import java.io.IOException;

import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Participant;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;

public abstract class AbstractCreateFlowElementFeature<T extends FlowElement> extends AbstractCreateFeature {
	
	protected FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};
	
	public AbstractCreateFlowElementFeature(IFeatureProvider fp, String name, String description) {
	    super(fp, name, description);
    }
	
	@Override
    public boolean canCreate(ICreateContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = support.isTargetLane(context) && support.isTargetLaneOnTop(context); 
		boolean intoParticipant = support.isTargetParticipant(context);
		return intoDiagram || intoLane || intoParticipant;
    }

	@Override
    public Object[] create(ICreateContext context) {
		T element = null;
		try {
			ModelHandler handler = support.getModelHanderInstance(getDiagram());
			element = createFlowElement(context);
			handler.addFlowElement(getParticipant(context, handler), element);
		} catch (IOException e) {
			Activator.logError(e);
		}
		addGraphicalRepresentation(context, element);
		return new Object[] { element };
	}
	
	protected abstract T createFlowElement(ICreateContext context);
	
	private Participant getParticipant(ICreateContext context, ModelHandler handler) {
		if(context.getTargetContainer().equals(getDiagram())) {
			return handler.getInternalParticipant();
		} else {
			Object bo = getBusinessObjectForPictogramElement(context.getTargetContainer());
			return handler.getParticipant(bo);
		}
	}
}
