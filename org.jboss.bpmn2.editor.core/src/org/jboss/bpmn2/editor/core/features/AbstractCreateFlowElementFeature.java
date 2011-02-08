package org.jboss.bpmn2.editor.core.features;

import java.io.IOException;

import org.eclipse.bpmn2.FlowElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;

public abstract class AbstractCreateFlowElementFeature<T extends FlowElement> extends AbstractCreateFeature {
	
	protected FeatureSupport support = new FeatureSupport() {
		@Override
		protected Object getBusinessObject(PictogramElement element) {
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
		return intoDiagram || intoLane;
    }

	@Override
    public Object[] create(ICreateContext context) {
		T element = null;
		try {
			ModelHandler handler = support.getModelHanderInstance(getDiagram());
			element = create(context, handler);
		} catch (IOException e) {
			Activator.logError(e);
		}
		addGraphicalRepresentation(context, element);
		return new Object[] { element };
	}
	
	protected abstract T create(ICreateContext context, ModelHandler handler);
}