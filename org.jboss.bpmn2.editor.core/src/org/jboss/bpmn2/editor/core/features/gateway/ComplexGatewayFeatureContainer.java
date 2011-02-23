package org.jboss.bpmn2.editor.core.features.gateway;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.ComplexGateway;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.ShapeUtil;
import org.jboss.bpmn2.editor.core.features.ShapeUtil.Asterisk;
import org.jboss.bpmn2.editor.core.features.StyleUtil;

public class ComplexGatewayFeatureContainer extends AbstractGatewayFeatureContainer {

	@Override
    public boolean canApplyTo(BaseElement element) {
	    return element instanceof ComplexGateway;
    }

	@Override
    public ICreateFeature getCreateFeature(IFeatureProvider fp) {
	    return new CreateComplexGatewayFeature(fp);
    }

	@Override
    public IAddFeature getAddFeature(IFeatureProvider fp) {
	    return new DefaultAddGatewayFeature(fp) {
	    	@Override
	    	protected void decorateGateway(ContainerShape container) {
	    		Asterisk asterisk = ShapeUtil.createGatewayAsterisk(container);
	    		asterisk.horizontal.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
	    		asterisk.vertical.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
	    		asterisk.diagonalAsc.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
	    		asterisk.diagonalDesc.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));
	    	}
	    };
    }
	
	public class CreateComplexGatewayFeature extends AbstractCreateGatewayFeature {

		public CreateComplexGatewayFeature(IFeatureProvider fp) {
		    super(fp, "Complex Gateway", "Used for modeling complex synchronization behavior");
	    }

		@Override
	    protected ComplexGateway createFlowElement(ICreateContext context) {
		    return ModelHandler.FACTORY.createComplexGateway();
	    }

		@Override
        protected String getStencilImageId() {
	        return ImageProvider.IMG_16_COMPLEX_GATEWAY;
        }
	}
}