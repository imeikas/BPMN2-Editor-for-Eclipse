package org.jboss.bpmn2.editor.core.features.activity;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;
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
}