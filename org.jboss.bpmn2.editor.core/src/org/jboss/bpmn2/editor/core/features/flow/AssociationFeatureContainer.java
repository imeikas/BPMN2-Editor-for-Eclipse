package org.jboss.bpmn2.editor.core.features.flow;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;
import org.jboss.bpmn2.editor.core.features.ConnectionFeatureContainer;

public class AssociationFeatureContainer extends ConnectionFeatureContainer {

	@Override
    public boolean canApplyTo(BaseElement element) {
	    return element instanceof Association;
    }

	@Override
    public IAddFeature getAddFeature(IFeatureProvider fp) {
	    return new AbstractAddFlowFeature(fp) {
			
	    	@Override
	    	void decorateConnectionLine(Polyline connectionLine) {
	    		connectionLine.setLineWidth(2);
	    		connectionLine.setLineStyle(LineStyle.DOT);
	    	}
	    	
			@Override
			Class<? extends BaseElement> getBoClass() {
				return Association.class;
			}
		};
    }

	@Override
    public ICreateConnectionFeature getCreateConnectionFeature(IFeatureProvider fp) {
	    return new CreateAssociationFeature(fp);
    }
	
	public static class CreateAssociationFeature extends AbstractCreateFlowFeature<BaseElement> {


		public CreateAssociationFeature(IFeatureProvider fp) {
	        super(fp, "Association", "Associate information with artifacts and flow objects");
        }

		@Override
        BaseElement createFlow(ModelHandler mh, BaseElement source, BaseElement target) {
			return mh.createAssociation(source, target);
        }

		@Override
        BaseElement getFlowNode(Anchor anchor) {
			if (anchor != null) {
				return (BaseElement) BusinessObjectUtil.getFirstElementOfType(anchor.getParent(), BaseElement.class);
			}
			return null;
        }
		
		@Override
		String getStencilImageId() {
			return ImageProvider.IMG_16_ASSOCIATION;
		}
	}
}