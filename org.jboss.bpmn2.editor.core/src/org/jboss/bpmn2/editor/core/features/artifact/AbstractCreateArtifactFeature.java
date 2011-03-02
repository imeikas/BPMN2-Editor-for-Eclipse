package org.jboss.bpmn2.editor.core.features.artifact;

import java.io.IOException;

import org.eclipse.bpmn2.Artifact;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public abstract class AbstractCreateArtifactFeature extends AbstractCreateFeature {
	
	protected FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};
	
	public AbstractCreateArtifactFeature(IFeatureProvider fp, String name, String description) {
	    super(fp, name, description);
    }

	@Override
    public boolean canCreate(ICreateContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = support.isTargetLane(context) && support.isTargetLaneOnTop(context);
		boolean intoParticipant = support.isTargetParticipant(context);
		return intoDiagram || intoLane || intoParticipant;
    }

	@Override
    public Object[] create(ICreateContext context) {
		Artifact artifact = null;
		try {
			ModelHandler handler = support.getModelHanderInstance(getDiagram());
			artifact = createArtifact(context);
			handler.addArtifact(support.getTargetParticipant(context, handler), artifact);
		} catch (IOException e) {
			Activator.logError(e);
		}
		addGraphicalRepresentation(context, artifact);
		return new Object[] { artifact };
	}
	
	abstract Artifact createArtifact(ICreateContext context);
	
	abstract String getStencilImageId();
	
	@Override
	public String getCreateImageId() {
	    return getStencilImageId();
	}
	
	@Override
	public String getCreateLargeImageId() {
	    return getCreateImageId();
	}
}