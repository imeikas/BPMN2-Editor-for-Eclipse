package org.jboss.bpmn2.editor.core.features.artifact;

import java.io.IOException;

import org.eclipse.bpmn2.Artifact;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public abstract class AbstractCreateArtifactFeature extends AbstractCreateFeature {
	
	public AbstractCreateArtifactFeature(IFeatureProvider fp, String name, String description) {
	    super(fp, name, description);
    }

	@Override
    public boolean canCreate(ICreateContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context) && FeatureSupport.isTargetLaneOnTop(context);
		boolean intoParticipant = FeatureSupport.isTargetParticipant(context);
		return intoDiagram || intoLane || intoParticipant;
    }

	@Override
    public Object[] create(ICreateContext context) {
		Artifact artifact = null;
		try {
			ModelHandler handler = FeatureSupport.getModelHanderInstance(getDiagram());
			artifact = createArtifact(context);
			handler.addArtifact(FeatureSupport.getTargetParticipant(context, handler), artifact);
		} catch (IOException e) {
			Activator.logError(e);
		}
		addGraphicalRepresentation(context, artifact);
		return new Object[] { artifact };
	}
	
	public abstract Artifact createArtifact(ICreateContext context);
	
	public abstract String getStencilImageId();
	
	@Override
	public String getCreateImageId() {
	    return getStencilImageId();
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId();
	}
}