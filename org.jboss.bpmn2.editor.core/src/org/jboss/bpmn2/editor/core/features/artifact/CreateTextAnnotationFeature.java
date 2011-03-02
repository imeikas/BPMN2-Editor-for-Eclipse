package org.jboss.bpmn2.editor.core.features.artifact;

import java.io.IOException;

import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public class CreateTextAnnotationFeature extends AbstractCreateFeature {

	private FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};

	public CreateTextAnnotationFeature(IFeatureProvider fp) {
		super(fp, "Annotation", "Provide additional information");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = support.isTargetLane(context) && support.isTargetLaneOnTop(context);
		return intoDiagram || intoLane;
	}

	@Override
	public Object[] create(ICreateContext context) {
		TextAnnotation annotation = null;

		try {
			ModelHandler mh = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
			annotation = mh.addArtifact(support.getTargetParticipant(context, mh),
			        ModelHandler.FACTORY.createTextAnnotation());
			annotation.setText("Enter your comment here");
		} catch (IOException e) {
			Activator.logError(e);
		}

		addGraphicalRepresentation(context, annotation);

		return new Object[] { annotation };
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
