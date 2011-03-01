package org.jboss.bpmn2.editor.core.features.artifact;

import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.jboss.bpmn2.editor.core.features.BusinessObjectUtil;

public class DirectEditTextAnnotationFeature extends AbstractDirectEditingFeature {

	public DirectEditTextAnnotationFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public int getEditingType() {
		return TYPE_TEXT;
	}

	@Override
	public String getInitialValue(IDirectEditingContext context) {
		TextAnnotation annotation = (TextAnnotation) BusinessObjectUtil.getFirstElementOfType(
				context.getPictogramElement(), TextAnnotation.class);
		return annotation.getText();
	}

	@Override
	public void setValue(String value, IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		TextAnnotation annotation = (TextAnnotation) BusinessObjectUtil.getFirstElementOfType(pe, TextAnnotation.class);
		annotation.setText(value);
		updatePictogramElement(((Shape) pe).getContainer());
	}

	@Override
	public boolean canDirectEdit(IDirectEditingContext context) {
		Object bo = BusinessObjectUtil.getFirstElementOfType(context.getPictogramElement(), TextAnnotation.class);
		GraphicsAlgorithm ga = context.getGraphicsAlgorithm();
		return bo instanceof TextAnnotation && ga instanceof MultiText;
	}
}
