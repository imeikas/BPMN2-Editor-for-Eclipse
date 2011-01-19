package org.jboss.bpmn2.editor.core.features;

import java.io.IOException;

import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.emf.common.util.URI;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.impl.AbstractCreateConnectionFeature;
import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.util.IColorConstant;
import org.jboss.bpmn2.editor.core.ModelHandler;

public class CreateSequenceFlowFeature extends AbstractCreateConnectionFeature {

	public CreateSequenceFlowFeature(IFeatureProvider fp) {
		super(fp, "Sequence Flow",
		        "A Sequence Flow is used to show the order that Activities will be performed in a Process");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canCreate(ICreateConnectionContext context) {
		FlowNode source = getFlowNode(context.getSourceAnchor());
		FlowNode target = getFlowNode(context.getTargetAnchor());
		if (source != null && target != null)
			return true;
		return false;
	}

	private FlowNode getFlowNode(Anchor anchor) {
		if (anchor != null) {
			Object bo = getBusinessObjectForPictogramElement(anchor.getParent());
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

		if (source != null && target != null) {
			URI uri = getDiagram().eResource().getURI();
			uri = uri.trimFragment();
			uri = uri.trimFileExtension();
			uri = uri.appendFileExtension("bpmn2"); // FIXME: move into some Util

			try {
				ModelHandler mh = null;
				mh = ModelHandler.getModelHandler(uri);
				SequenceFlow flow = mh.createSequenceFlow();
				flow.setSourceRef(source);
				flow.setTargetRef(target);

				AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(),
				        context.getTargetAnchor());
				addContext.setNewObject(flow);

				return (Connection) getFeatureProvider().addIfPossible(addContext);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean canStartConnection(ICreateConnectionContext context) {
		return getFlowNode(context.getSourceAnchor()) != null;
	}

}
