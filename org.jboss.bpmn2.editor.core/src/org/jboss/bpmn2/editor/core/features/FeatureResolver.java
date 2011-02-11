package org.jboss.bpmn2.editor.core.features;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;

public interface FeatureResolver {
	
	AbstractCreateFeature getCreateFeature(IFeatureProvider fp, BaseElement e);
	
	AbstractAddShapeFeature getAddFeature(IFeatureProvider fp, BaseElement e);
	
	AbstractDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp, BaseElement e);
	
	AbstractLayoutFeature getLayoutFeature(IFeatureProvider fp, BaseElement e);
	
	AbstractUpdateFeature getUpdateFeature(IFeatureProvider fp, BaseElement e);
}