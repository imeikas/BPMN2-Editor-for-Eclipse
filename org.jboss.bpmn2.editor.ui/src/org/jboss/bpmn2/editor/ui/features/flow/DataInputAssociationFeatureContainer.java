package org.jboss.bpmn2.editor.ui.features.flow;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataInputAssociation;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.jboss.bpmn2.editor.core.features.ConnectionFeatureContainer;

public class DataInputAssociationFeatureContainer extends ConnectionFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof DataInputAssociation;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return null;
	}

	@Override
	public ICreateConnectionFeature getCreateConnectionFeature(IFeatureProvider fp) {
		return null;
	}
}