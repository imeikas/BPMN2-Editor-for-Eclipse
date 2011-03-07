package org.jboss.bpmn2.editor.core.features;

import java.io.IOException;

import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.utils.FeatureSupport;

public abstract class AbstractCreateFlowElementFeature<T extends FlowElement> extends AbstractCreateFeature {

	public AbstractCreateFlowElementFeature(IFeatureProvider fp, String name, String description) {
		super(fp, name, description);
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context) && FeatureSupport.isTargetLaneOnTop(context);
		boolean intoParticipant = FeatureSupport.isTargetParticipant(context);
		boolean intoSubProcess = FeatureSupport.isTargetSubProcess(context);
		return intoDiagram || intoLane || intoParticipant || intoSubProcess;
	}

	@Override
	public Object[] create(ICreateContext context) {
		T element = null;
		try {
			ModelHandler handler = FeatureSupport.getModelHanderInstance(getDiagram());
			element = createFlowElement(context);
			element.setId(EcoreUtil.generateUUID());
			if (FeatureSupport.isTargetLane(context) && element instanceof FlowNode) {
				((FlowNode) element).getLanes().add(
						(Lane) getBusinessObjectForPictogramElement(context.getTargetContainer()));
			}

			handler.addFlowElement(getBusinessObjectForPictogramElement(context.getTargetContainer()), element);
		} catch (IOException e) {
			Activator.logError(e);
		}
		addGraphicalRepresentation(context, element);
		return new Object[] { element };
	}

	protected abstract T createFlowElement(ICreateContext context);
}