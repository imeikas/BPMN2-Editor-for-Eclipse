package org.jboss.bpmn2.editor.core.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;

public abstract class AbstractCreateFlowElementFeature extends AbstractCreateFeature {

	public AbstractCreateFlowElementFeature(IFeatureProvider fp, String name, String description) {
	    super(fp, name, description);
    }
	
	//TODO
	
	@Override
    public boolean canCreate(ICreateContext context) {
	    // TODO Auto-generated method stub
	    return false;
    }

	@Override
    public Object[] create(ICreateContext context) {
	    // TODO Auto-generated method stub
	    return null;
    }
}
