package org.jboss.bpmn2.editor.core.features.activity;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.jboss.bpmn2.editor.core.features.DefaultBPMNResizeFeature;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;
import org.jboss.bpmn2.editor.core.features.MoveFlowNodeFeature;
import org.jboss.bpmn2.editor.core.features.MultiUpdateFeature;

public abstract class AbstractActivityFeatureContainer implements FeatureContainer {

	@Override
    public MultiUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		ActivityCompensateMarkerUpdateFeature compensateMarkerUpdateFeature = 
			new ActivityCompensateMarkerUpdateFeature(fp);
		ActivityLoopAndMultiInstanceMarkerUpdateFeature loopAndMultiInstanceUpdateFeature = 
			new ActivityLoopAndMultiInstanceMarkerUpdateFeature(fp);
		MultiUpdateFeature multiUpdate = new MultiUpdateFeature(fp);
		multiUpdate.addUpdateFeature(compensateMarkerUpdateFeature);
		multiUpdate.addUpdateFeature(loopAndMultiInstanceUpdateFeature);
	    return multiUpdate;
    }
	
	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
	    return new DefaultBPMNResizeFeature(fp);
	}
	
	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
		return new MoveFlowNodeFeature(fp);
	}
}