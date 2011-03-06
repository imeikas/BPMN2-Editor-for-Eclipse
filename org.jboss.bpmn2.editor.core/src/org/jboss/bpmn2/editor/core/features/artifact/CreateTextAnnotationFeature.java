package org.jboss.bpmn2.editor.core.features.artifact;

import java.io.IOException;

import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public class CreateTextAnnotationFeature extends AbstractCreateFeature {

	public CreateTextAnnotationFeature(IFeatureProvider fp) {
		super(fp, "Annotation", "Provide additional information");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context) && FeatureSupport.isTargetLaneOnTop(context);
		return intoDiagram || intoLane;
	}

	@Override
	public Object[] create(ICreateContext context) {
		TextAnnotation ta = null;

		try {
			ModelHandler mh = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
			ta = ModelHandler.FACTORY.createTextAnnotation();
			ta.setId(EcoreUtil.generateUUID());
			mh.addArtifact(FeatureSupport.getTargetParticipant(context, mh), ta);
			ta.setText("Enter your comment here");
		} catch (IOException e) {
			Activator.logError(e);
		}

		addGraphicalRepresentation(context, ta);

		return new Object[] { ta };
	}

	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_TEXT_ANNOTATION;
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); // FIXME
	}
}
