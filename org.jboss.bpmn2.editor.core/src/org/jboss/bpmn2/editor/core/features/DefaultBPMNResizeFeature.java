package org.jboss.bpmn2.editor.core.features;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.jboss.bpmn2.editor.core.di.DIUtils;

public class DefaultBPMNResizeFeature extends DefaultResizeShapeFeature {

	public DefaultBPMNResizeFeature(IFeatureProvider fp) {
	    super(fp);
    }
	
	@Override
	public void resizeShape(IResizeShapeContext context) {
	    super.resizeShape(context);
	    DIUtils.updateDIShape(getDiagram(), context.getPictogramElement(), BaseElement.class);
	}
}