package org.jboss.bpmn2.editor.core.features;

import java.io.IOException;

import org.eclipse.bpmn2.Lane;
import org.eclipse.graphiti.features.context.ITargetContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;

public abstract class FeatureSupport {
	
	protected abstract Object getBusinessObject(PictogramElement element);
	
	public boolean isTargetLane(ITargetContext context) {
		Object bo = getBusinessObject(context.getTargetContainer());
		return bo != null && bo instanceof Lane;
	}
	
	public boolean isLaneOnTop(Lane lane) {
		return lane.getChildLaneSet() == null || lane.getChildLaneSet().getLanes().isEmpty();
	}
	
	public boolean isTargetLaneOnTop(ITargetContext context) {
		Lane lane = (Lane) getBusinessObject(context.getTargetContainer());
		return lane.getChildLaneSet() == null || lane.getChildLaneSet().getLanes().isEmpty();
	}
	
	public ModelHandler getModelHanderInstance(Diagram diagram) throws IOException {
		return ModelHandlerLocator.getModelHandler(diagram.eResource());
	}
}
