package org.jboss.bpmn2.editor.core.diagram;

import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;

public class MainBPMNDiagramTypeProvider extends AbstractDiagramTypeProvider {

	public MainBPMNDiagramTypeProvider() {
		super();
		setFeatureProvider(new BPMNFeatureProvider(this));
	}
}
