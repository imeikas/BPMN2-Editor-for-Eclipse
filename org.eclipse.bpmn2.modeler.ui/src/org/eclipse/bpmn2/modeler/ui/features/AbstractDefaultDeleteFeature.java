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
package org.eclipse.bpmn2.modeler.ui.features;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.features.DefaultBpmnDeleteFeature;
import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.impl.DeleteContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class AbstractDefaultDeleteFeature extends DefaultBpmnDeleteFeature {
	public AbstractDefaultDeleteFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public void delete(IDeleteContext context) {
		IFeatureProvider fp = getFeatureProvider();
		PictogramElement pictogramElement = context.getPictogramElement();
		if (pictogramElement instanceof ContainerShape) {
			ContainerShape cShape = (ContainerShape) pictogramElement;
			EList<Anchor> anchors = cShape.getAnchors();
			for (Anchor anchor : anchors) {
				deleteConnections(fp, anchor.getIncomingConnections());
				deleteConnections(fp, anchor.getOutgoingConnections());
			}
			deleteContainer(fp, cShape);
		}
		super.delete(context);
	}

	private void deleteContainer(IFeatureProvider fp, ContainerShape cShape) {
		Object[] children = cShape.getChildren().toArray();
		for (Object shape : children) {
			if (shape instanceof ContainerShape) {
				DeleteContext context = new DeleteContext((PictogramElement) shape);
				fp.getDeleteFeature(context).delete(context);
			}
		}
	}

	private void deleteConnections(IFeatureProvider fp, EList<Connection> connections) {
		List<Connection> con = new ArrayList<Connection>();
		con.addAll(connections);
		for (Connection connection : con) {
			IDeleteContext conDelete = new DeleteContext(connection);
			fp.getDeleteFeature(conDelete).delete(conDelete);
		}
	}
}
