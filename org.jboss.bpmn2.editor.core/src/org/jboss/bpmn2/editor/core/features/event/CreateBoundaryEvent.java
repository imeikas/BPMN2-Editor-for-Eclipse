package org.jboss.bpmn2.editor.core.features.event;

import java.io.IOException;

import org.eclipse.bpmn2.Activity;
import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.jboss.bpmn2.editor.core.Activator;
import org.jboss.bpmn2.editor.core.ImageProvider;
import org.jboss.bpmn2.editor.core.ModelHandler;
import org.jboss.bpmn2.editor.core.features.FeatureSupport;

public class CreateBoundaryEvent extends AbstractCreateFeature {

	protected final boolean interrupting;

	protected FeatureSupport support = new FeatureSupport() {
		@Override
		public Object getBusinessObject(PictogramElement element) {
			return getBusinessObjectForPictogramElement(element);
		}
	};

	public CreateBoundaryEvent(IFeatureProvider fp, String name, String description, boolean interrupting) {
		super(fp, name, description);
		this.interrupting = interrupting;
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		Object o = getBusinessObjectForPictogramElement(context.getTargetContainer());
		if (o == null || !(o instanceof Activity))
			return false;
		return true;
	}

	@Override
	public Object[] create(ICreateContext context) {
		BoundaryEvent event = null;
		try {
			ModelHandler handler = support.getModelHanderInstance(getDiagram());
			event = ModelHandler.FACTORY.createBoundaryEvent();
			event.setCancelActivity(interrupting);
			handler.addFlowElement(support.getTargetParticipant(context, handler), event);
		} catch (IOException e) {
			Activator.logError(e);
		}
		addGraphicalRepresentation(context, event);
		return new Object[] { event };
	}

	@Override
	public String getCreateImageId() {
		return ImageProvider.IMG_16_BOUNDARY_EVENT;
	}

	@Override
	public String getCreateLargeImageId() {
		return getCreateImageId(); // FIXME
	}
}
