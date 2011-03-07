package org.jboss.bpmn2.editor.ui.features.activity;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.impl.DeleteContext;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.DefaultDeleteFeature;

public class ActivityDefaultDeleteFeature extends DefaultDeleteFeature {
	public ActivityDefaultDeleteFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public void delete(IDeleteContext context) {
		IFeatureProvider fp = getFeatureProvider();
		PictogramElement pictogramElement = context.getPictogramElement();
		if (pictogramElement instanceof ContainerShape) {
			EList<Anchor> anchors = ((ContainerShape) pictogramElement).getAnchors();
			for (Anchor anchor : anchors) {
				deleteConnections(fp, anchor.getIncomingConnections());
				deleteConnections(fp, anchor.getOutgoingConnections());
			}
		}
		super.delete(context);
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