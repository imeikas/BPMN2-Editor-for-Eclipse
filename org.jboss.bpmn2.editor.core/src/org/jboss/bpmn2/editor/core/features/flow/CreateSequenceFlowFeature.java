package org.jboss.bpmn2.editor.core.features.flow;

import java.io.IOException;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.SequenceFlow;
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

public class CreateSequenceFlowFeature extends AbstractCreateConnectionFeature {

	public CreateSequenceFlowFeature(IFeatureProvider fp) {
		super(fp, "Sequence Flow",
				"A Sequence Flow is used to show the order that Activities will be performed in a Process");
	}

	@Override
	public boolean canCreate(ICreateConnectionContext context) {
		FlowNode source = getFlowNode(context.getSourceAnchor());
		FlowNode target = getFlowNode(context.getTargetAnchor());

		return source != null && target != null;
	}

	private FlowNode getFlowNode(Anchor anchor) {
		if (anchor != null) {
			Object bo = BusinessObjectUtil.getFirstElementOfType(anchor.getParent(), BaseElement.class);
			if (bo instanceof FlowNode) {
				return (FlowNode) bo;
			}
		}
		return null;
	}

	@Override
	public Connection create(ICreateConnectionContext context) {
		FlowNode source = getFlowNode(context.getSourceAnchor());
		FlowNode target = getFlowNode(context.getTargetAnchor());

		if (source != null || target != null) {
			try {
				ModelHandler mh = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
				SequenceFlow flow = mh.createSequenceFlow(source, target);

				AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(),
						context.getTargetAnchor());
				addContext.setNewObject(flow);

				return (Connection) getFeatureProvider().addIfPossible(addContext);
			} catch (IOException e) {
				Activator.logError(e);
			}
		}
		return null;
	}

	@Override
	public boolean canStartConnection(ICreateConnectionContext context) {
		return getFlowNode(context.getSourceAnchor()) != null;
	}

	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_SEQUENCE_FLOW;
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); // FIXME
	}
}
