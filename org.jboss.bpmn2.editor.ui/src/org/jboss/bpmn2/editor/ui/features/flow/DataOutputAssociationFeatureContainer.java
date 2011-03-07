package org.jboss.bpmn2.editor.ui.features.flow;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.DataOutputAssociation;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.jboss.bpmn2.editor.core.features.ConnectionFeatureContainer;

public class DataOutputAssociationFeatureContainer extends ConnectionFeatureContainer {

	@Override
	public boolean canApplyTo(BaseElement element) {
		return element instanceof DataOutputAssociation;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICreateConnectionFeature getCreateConnectionFeature(IFeatureProvider fp) {
		return null;
	}
}