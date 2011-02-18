package org.jboss.bpmn2.editor.core.features.event.definitions;

import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.util.IColorConstant;


abstract class DecorationAlgorithm {
	
	abstract Shape draw(ContainerShape shape);
	
	abstract Color manageColor(IColorConstant colorCOnstant);
}