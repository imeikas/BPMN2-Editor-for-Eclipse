package org.jboss.bpmn2.editor.core.diagram;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;

/**
 * Determines what kinds of business objects can be added to a diagram.
 * 
 * @author imeikas
 * 
 */
public class BPMNFeatureProvider extends DefaultFeatureProvider {

	public BPMNFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);
	}
}
