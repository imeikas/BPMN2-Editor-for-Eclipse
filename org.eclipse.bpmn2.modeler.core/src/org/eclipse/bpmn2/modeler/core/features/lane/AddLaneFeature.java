/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 *  All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.eclipse.bpmn2.modeler.core.features.lane;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.LaneSet;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SubProcess;
import org.eclipse.bpmn2.modeler.core.di.DIImport;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmnAddFeature;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ITargetContext;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.ILayoutService;
import org.eclipse.graphiti.services.IPeCreateService;

public class AddLaneFeature extends AbstractBpmnAddFeature {

	public AddLaneFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean isLane = context.getNewObject() instanceof Lane;
		boolean intoDiagram = context.getTargetContainer().equals(getDiagram());
		boolean intoLane = FeatureSupport.isTargetLane(context);
		boolean intoParticipant = FeatureSupport.isTargetParticipant(context);
		boolean intoSubprocess = FeatureSupport.isTargetSubProcess(context);
		return isLane && (intoDiagram || intoLane || intoParticipant || intoSubprocess);
	}

	@Override
	public PictogramElement add(IAddContext context) {
		Lane lane = (Lane) context.getNewObject();

		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(context.getTargetContainer(), true);
		IGaService gaService = Graphiti.getGaService();

		int width = context.getWidth() > 0 ? context.getWidth() : 600;
		int height = context.getHeight() > 0 ? context.getHeight() : 100;

		Rectangle rect = gaService.createRectangle(containerShape);

		StyleUtil.applyBGStyle(rect, this);

		if (FeatureSupport.isTargetLane(context) || FeatureSupport.isTargetParticipant(context)) {
			GraphicsAlgorithm ga = context.getTargetContainer().getGraphicsAlgorithm();

			if (getNumberOfLanes(context) == 1) {
				gaService.setLocationAndSize(rect, 15, 0, width - 15, height);
				for (Shape s : getFlowNodeShapes(context, lane)) {
					Graphiti.getPeService().sendToFront(s);
					s.setContainer(containerShape);
				}
			} else {
				if (context.getWidth() == -1 || context.getHeight() == -1) {
					gaService.setLocationAndSize(rect, 15, ga.getWidth() - 1, ga.getHeight() - 15, height);
					// gaService.setLocationAndSize(rect, context.getX(), context.getY(), width, height);
				} else {
					ILayoutService layoutService = Graphiti.getLayoutService();
					ILocation loc = layoutService.getLocationRelativeToDiagram(containerShape);
					int x = context.getX() - loc.getX();
					int y = context.getY() - loc.getY();
					gaService.setLocationAndSize(rect, x - 15, y, ga.getWidth() - 15, height);
				}
			}
			containerShape.setContainer(context.getTargetContainer());
		} else {
			gaService.setLocationAndSize(rect, context.getX(), context.getY(), width, height);
		}

		Shape textShape = peCreateService.createShape(containerShape, false);
		Text text = gaService.createText(textShape, lane.getName());
		text.setStyle(StyleUtil.getStyleForText(getDiagram()));
		text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setAngle(-90);
		gaService.setLocationAndSize(text, 0, 0, 15, height);

		createDIShape(containerShape, lane);
		link(textShape, lane);

		peCreateService.createChopboxAnchor(containerShape);
		AnchorUtil.addFixedPointAnchors(containerShape, rect);

		if (context.getProperty(DIImport.IMPORT_PROPERTY) == null
				&& (FeatureSupport.isTargetLane(context) || FeatureSupport.isTargetParticipant(context))) {
			FeatureSupport.redraw(context.getTargetContainer());
		}
		return containerShape;
	}

	private List<Shape> getFlowNodeShapes(IAddContext context, Lane lane) {
		List<FlowNode> nodes = lane.getFlowNodeRefs();
		List<Shape> shapes = new ArrayList<Shape>();
		for (Shape s : context.getTargetContainer().getChildren()) {
			Object bo = getBusinessObjectForPictogramElement(s);
			if (bo != null && nodes.contains(bo)) {
				shapes.add(s);
			}
		}
		return shapes;
	}

	private int getNumberOfLanes(ITargetContext context) {
		ContainerShape targetContainer = context.getTargetContainer();
		Object bo = getBusinessObjectForPictogramElement(targetContainer);
		if (bo instanceof Lane) {
			Lane lane = (Lane) bo;
			return lane.getChildLaneSet().getLanes().size();
		} else if (bo instanceof Participant) {
			List<LaneSet> laneSets = ((Participant) bo).getProcessRef().getLaneSets();
			if (laneSets.size() > 0) {
				return laneSets.get(0).getLanes().size();
			}
			return laneSets.size();
		} else if (bo instanceof SubProcess) {
			List<LaneSet> laneSets = ((SubProcess) bo).getLaneSets();
			if (laneSets.size() > 0) {
				return laneSets.get(0).getLanes().size();
			}
			return laneSets.size();
		}
		return 0;
	}
}