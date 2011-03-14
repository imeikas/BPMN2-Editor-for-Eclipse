package org.jboss.bpmn2.editor.core.features.choreography;

import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.BODY_BAND_TEXT;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.BOTTOM_BAND_TEXT;
import static org.jboss.bpmn2.editor.core.features.choreography.ChoreographyProperties.TOP_BAND_TEXT;

import org.eclipse.bpmn2.ChoreographyActivity;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

public class AddSubChoreographyFeature extends AbstractAddChoreographyFeature {

	public AddSubChoreographyFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void createTexts(ContainerShape containerShape, ChoreographyActivity choreography, int w, int h,
	        int bandHeight) {
		createText(containerShape, "Participant A", 0, 0, w, bandHeight, TOP_BAND_TEXT);
		createText(containerShape, "Participant B", 0, h - bandHeight, w, bandHeight, BOTTOM_BAND_TEXT);
		createText(containerShape, choreography.getName(), 0, bandHeight - 5, w, 15, BODY_BAND_TEXT);
	}
}