package org.jboss.bpmn2.editor.ui.features.choreography;

import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.jboss.bpmn2.editor.core.features.DefaultBPMNResizeFeature;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;
import org.jboss.bpmn2.editor.core.features.MoveFlowNodeFeature;
import org.jboss.bpmn2.editor.core.features.MultiUpdateFeature;
import org.jboss.bpmn2.editor.core.features.choreography.LayoutChoreographyFeature;
import org.jboss.bpmn2.editor.core.features.choreography.UpdateChoreographyNameFeature;
import org.jboss.bpmn2.editor.core.features.choreography.UpdateChoreographyParticipantRefsFeature;
import org.jboss.bpmn2.editor.core.features.choreography.UpdateInitiatingParticipantFeature;
import org.jboss.bpmn2.editor.ui.features.AbstractDefaultDeleteFeature;

public abstract class AbstractChoreographyFeatureContainer implements FeatureContainer {

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
		MultiUpdateFeature updateFeature = new MultiUpdateFeature(fp);
		updateFeature.addUpdateFeature(new UpdateChoreographyNameFeature(fp));
		updateFeature.addUpdateFeature(new UpdateChoreographyParticipantRefsFeature(fp));
		updateFeature.addUpdateFeature(new UpdateInitiatingParticipantFeature(fp));
		return updateFeature;
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider fp) {
		return new LayoutChoreographyFeature(fp);
	}

	@Override
	public IMoveShapeFeature getMoveFeature(IFeatureProvider fp) {
		return new MoveFlowNodeFeature(fp);
	}

	@Override
	public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
		return new DefaultBPMNResizeFeature(fp);
	}

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider fp) {
		return new AbstractDefaultDeleteFeature(fp);
	}
}