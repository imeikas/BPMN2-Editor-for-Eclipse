package org.jboss.bpmn2.editor.core.features.event.definitions;

import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.util.IColorConstant;


public abstract class DecorationAlgorithm {
	
	public abstract Shape draw(ContainerShape shape);
	
	public abstract Color manageColor(IColorConstant colorCOnstant);
}