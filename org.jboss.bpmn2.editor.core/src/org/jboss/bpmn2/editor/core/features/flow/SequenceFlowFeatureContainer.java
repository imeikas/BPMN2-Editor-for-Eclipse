package org.jboss.bpmn2.editor.core.features.flow;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.features.ConnectionFeatureContainer;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class SequenceFlowFeatureContainer extends ConnectionFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof SequenceFlow;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new AbstractAddFlowFeature(fp) {
			@Override
			Class<? extends BaseElement> getBoClass() {
				return SequenceFlow.class;
			}

			@Override
			void createConnectionDecorators(Connection connection) {
				int w = 3;
				int l = 8;

				ConnectionDecorator decorator = Graphiti.getPeService().createConnectionDecorator(connection, false,
				        1.0, true);

				IGaService gaService = Graphiti.getGaService();
				Polyline arrow = gaService.createPolygon(decorator, new int[] { -l, w, 0, 0, -l, -w, -l, w });

				arrow.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				arrow.setLineWidth(2);
			}
		};
	}

	@Override
	public ICreateConnectionFeature getCreateConnectionFeature(IFeatureProvider fp) {
		return new CreateSequenceFlowFeature(fp);
	}

	public static class CreateSequenceFlowFeature extends AbstractCreateFlowFeature<FlowNode> {

		public CreateSequenceFlowFeature(IFeatureProvider fp) {
			super(fp, "Sequence Flow",
			        "A Sequence Flow is used to show the order that Activities will be performed in a Process");
		}

		@Override
		BaseElement createFlow(ModelHandler mh, FlowNode source, FlowNode target) {
			SequenceFlow flow = mh.createSequenceFlow(source, target);
			flow.setName("Sequence Flow");
			return flow;
		}

		@Override
		FlowNode getFlowNode(Anchor anchor) {
			if (anchor != null) {
				return (FlowNode) BusinessObjectUtil.getFirstElementOfType(anchor, FlowNode.class);
			}
			return null;
		}

		@Override
		String getStencilImageId() {
			return ImageProvider.IMG_16_SEQUENCE_FLOW;
		}
	}
}