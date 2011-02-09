package org.jboss.bpmn2.editor.core.features.annotation;

import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.jboss.bpmn2.editor.core.features.AbstractBaseElementUpdateFeature;

public class UpdateTextAnnotationFeature extends AbstractBaseElementUpdateFeature {

	public UpdateTextAnnotationFeature(IFeatureProvider fp) {
	    super(fp);
    }

	@Override
    public boolean canUpdate(IUpdateContext context) {
	    return getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof TextAnnotation;
    }

}
