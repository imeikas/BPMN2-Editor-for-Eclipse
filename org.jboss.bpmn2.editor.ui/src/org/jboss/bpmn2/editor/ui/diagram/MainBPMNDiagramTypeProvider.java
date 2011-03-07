package org.jboss.bpmn2.editor.ui.diagram;

import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;

public class MainBPMNDiagramTypeProvider extends AbstractDiagramTypeProvider {
	private IToolBehaviorProvider[] toolBehaviorProviders;

	
	public MainBPMNDiagramTypeProvider() {
		super();
		setFeatureProvider(new BPMNFeatureProvider(this));
	}

	@Override
	public IToolBehaviorProvider[] getAvailableToolBehaviorProviders() {
		if (toolBehaviorProviders == null) {
			toolBehaviorProviders = new IToolBehaviorProvider[] { new BpmnToolBehaviourFeature(this) };
		}
		return toolBehaviorProviders;
	}
}
