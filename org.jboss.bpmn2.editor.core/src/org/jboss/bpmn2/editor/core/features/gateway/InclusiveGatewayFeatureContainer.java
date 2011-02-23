package org.jboss.bpmn2.editor.core.features.gateway;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Gateway;
import org.eclipse.bpmn2.InclusiveGateway;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class InclusiveGatewayFeatureContainer extends AbstractGatewayFeatureContainer {

	@Override
    public boolean canApplyTo(BaseElement element) {
	    return element instanceof InclusiveGateway;
    }

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new DefaultAddGatewayFeature(fp) {
			@Override
			protected void decorateGateway(Polygon gateway) {
				IGaService gaService = Graphiti.getGaService();
				Ellipse ellipse = gaService.createEllipse(gateway);
				ellipse.setFilled(false);
				ellipse.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
				ellipse.setLineWidth(2);
				gaService.setLocationAndSize(ellipse, 12, 12, 27, 27);
			}
		};
	}
	
	@Override
    public ICreateFeature getCreateFeature(IFeatureProvider fp) {
	    return new CreateInclusiveGatewayFeature(fp);
    }
	
	public static class CreateInclusiveGatewayFeature extends AbstractCreateGatewayFeature {
		
		public CreateInclusiveGatewayFeature(IFeatureProvider fp) {
		    super(fp, "Inclusive Gateway", "Used for creating alternative but also parallel paths");
	    }

		@Override
	    protected Gateway createFlowElement(ICreateContext context) {
			return ModelHandler.FACTORY.createInclusiveGateway();
	    }
		
		@Override
		protected String getStencilImageId() {
		    return ImageProvider.IMG_16_INCLUSIVE_GATEWAY;
		}
	}
}