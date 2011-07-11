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
package org.eclipse.bpmn2.modeler.core.features.flow;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.di.DIImport;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmnAddFeature;
import org.eclipse.bpmn2.modeler.core.features.UpdateBaseElementNameFeature;
import org.eclipse.bpmn2.modeler.core.utils.AnchorUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.core.utils.Tuple;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.FixPointAnchor;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;

public abstract class AbstractAddFlowFeature extends AbstractBpmnAddFeature {

	public AbstractAddFlowFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context instanceof IAddConnectionContext
				&& getBoClass().isAssignableFrom(context.getNewObject().getClass());
	}

	@Override
	public PictogramElement add(IAddContext context) {
		IPeService peService = Graphiti.getPeService();
		IGaService gaService = Graphiti.getGaService();

		BaseElement element = (BaseElement) context.getNewObject();
		IAddConnectionContext addConContext = (IAddConnectionContext) context;

		Connection connection = peService.createFreeFormConnection(getDiagram());

		Object importProp = context.getProperty(DIImport.IMPORT_PROPERTY);
		if (importProp != null && (Boolean) importProp) {
			connection.setStart(addConContext.getSourceAnchor());
			connection.setEnd(addConContext.getTargetAnchor());
		} else {
			ContainerShape sourceContainer = (ContainerShape) addConContext.getSourceAnchor().eContainer();
			ContainerShape targetContainer = (ContainerShape) addConContext.getTargetAnchor().eContainer();
			Tuple<FixPointAnchor, FixPointAnchor> anchors = AnchorUtil.getSourceAndTargetBoundaryAnchors(
					sourceContainer, targetContainer, connection);

			connection.setStart(anchors.getFirst());
			connection.setEnd(anchors.getSecond());
		}
		
		if (ModelUtil.hasName(element)) {
			ConnectionDecorator labelDecorator = Graphiti.getPeService().createConnectionDecorator(connection, true, 0.5, true);
			Text text = gaService.createText(labelDecorator, ModelUtil.getName(element));
			peService.setPropertyValue(labelDecorator, UpdateBaseElementNameFeature.TEXT_ELEMENT, Boolean.toString(true));
			text.setStyle(StyleUtil.getStyleForText(getDiagram()));
		}
		
		Polyline connectionLine = gaService.createPolyline(connection);
		connectionLine.setForeground(manageColor(StyleUtil.CLASS_FOREGROUND));

		decorateConnectionLine(connectionLine);

		createDIEdge(connection, element);
		createConnectionDecorators(connection);
		hook(addConContext, connection, element);

		return connection;
	}

	protected abstract Class<? extends BaseElement> getBoClass();

	protected void hook(IAddContext context, Connection connection, BaseElement element) {
	}

	protected void decorateConnectionLine(Polyline connectionLine) {
	}

	protected void createConnectionDecorators(Connection connection) {
	}
}