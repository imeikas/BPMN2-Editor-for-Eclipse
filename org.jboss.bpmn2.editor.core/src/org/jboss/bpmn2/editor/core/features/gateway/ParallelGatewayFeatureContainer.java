package org.jboss.bpmn2.editor.core.features.gateway;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class ParallelGatewayFeatureContainer extends AbstractGatewayFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof ParallelGateway;
	}

	@Override
	public ICreateFeature getCreateFeature(IFeatureProvider fp) {
		return new CreateParallelGatewayFeature(fp);
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new DefaultAddGatewayFeature(fp) {
			@Override
			protected void decorateGateway(Polygon gateway) {
				IGaService gaService = Graphiti.getGaService();

				Polyline verticalLine = gaService.createPolyline(gateway, new int[] { 24, 7, 24, 43 });
				verticalLine.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				verticalLine.setLineWidth(3);

				Polyline horizontalLine = gaService.createPolyline(gateway, new int[] { 7, 24, 43, 24 });
				horizontalLine.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				horizontalLine.setLineWidth(3);
			}
		};
	}

	public static class CreateParallelGatewayFeature extends AbstractCreateGatewayFeature {

		public CreateParallelGatewayFeature(IFeatureProvider fp) {
			super(fp, "Parallel Gateway", "Used to combine or create parallel flows");
		}

		@Override
		protected Gateway createFlowElement(ICreateContext context) {
			return ModelHandler.FACTORY.createParallelGateway();
		}

		@Override
		protected String getStencilImageId() {
			return ImageProvider.IMG_16_PARALLEL_GATEWAY;
		}
	}
}
