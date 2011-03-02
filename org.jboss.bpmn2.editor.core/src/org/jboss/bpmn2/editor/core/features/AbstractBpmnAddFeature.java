package org.jboss.bpmn2.editor.core.features;

import java.io.IOException;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.di.BPMNShape;
import org.eclipse.bpmn2.di.BpmnDiFactory;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.dc.DcFactory;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.services.Graphiti;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ModelHandlerLocator;

public abstract class AbstractBpmnAddFeature extends AbstractAddShapeFeature {

	public AbstractBpmnAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	protected void createDIShape(ContainerShape containerShape, BaseElement elem) {
		try {
			ILocation loc = Graphiti.getLayoutService().getLocationRelativeToDiagram(containerShape);
			BPMNShape shape = ModelHandlerLocator.getModelHandler(getDiagram().eResource()).findDIElement(getDiagram(),
					elem);
			if (shape == null) {
				EList<EObject> businessObjects = Graphiti.getLinkService().getLinkForPictogramElement(getDiagram())
						.getBusinessObjects();
				for (EObject eObject : businessObjects) {
					if (eObject instanceof BPMNDiagram) {
						BPMNDiagram bpmnDiagram = (BPMNDiagram) eObject;

						shape = BpmnDiFactory.eINSTANCE.createBPMNShape();
						shape.setBpmnElement(elem);
						Bounds bounds = DcFactory.eINSTANCE.createBounds();
						bounds.setHeight(containerShape.getGraphicsAlgorithm().getHeight());
						bounds.setWidth(containerShape.getGraphicsAlgorithm().getWidth());
						bounds.setX(loc.getX());
						bounds.setY(loc.getY());
						shape.setBounds(bounds);

						addShape(shape, bpmnDiagram);
					}
				}
			}
			link(containerShape, new Object[] { elem, shape });
		} catch (IOException e) {
			Activator.logError(e);
		}
	}

	private void addShape(BPMNShape shape, BPMNDiagram bpmnDiagram) {
		List<DiagramElement> elements = bpmnDiagram.getPlane().getPlaneElement();
		elements.add(shape);
	}

}