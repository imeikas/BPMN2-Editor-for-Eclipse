package org.jboss.bpmn2.editor.core.features.comment;

import java.io.IOException;

import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;

public class CreateTextAnnotationFeature extends AbstractCreateFeature  {

	public CreateTextAnnotationFeature(IFeatureProvider fp) {
	    super(fp, "Comment", "Comment");
    }

	@Override
    public boolean canCreate(ICreateContext context) {
		return context.getTargetContainer() instanceof Diagram;
    }

	@Override
    public Object[] create(ICreateContext context) {
		TextAnnotation annotation = null;

		try {
			ModelHandler mh = ModelHandlerLocator.getModelHandler(getDiagram().eResource());
			annotation = mh.addArtifact(ModelHandler.FACTORY.createTextAnnotation());
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
