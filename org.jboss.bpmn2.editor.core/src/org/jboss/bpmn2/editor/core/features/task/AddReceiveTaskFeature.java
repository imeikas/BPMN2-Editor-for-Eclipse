package org.jboss.bpmn2.editor.core.features.task;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.jboss.bpmn2.editor.core.ImageProvider;

public class AddReceiveTaskFeature extends AddTaskFeature {

	public AddReceiveTaskFeature(IFeatureProvider fp) {
	    super(fp);
    }
	
	@Override
	protected void decorateTask(RoundedRectangle rect, IAddContext context) {
		IGaService service = Graphiti.getGaService();
		Image img = service.createImage(rect, ImageProvider.IMG_16_RECEIVE_TASK);
		service.setLocationAndSize(img, 0, 0, 16, 16);
	}
}
