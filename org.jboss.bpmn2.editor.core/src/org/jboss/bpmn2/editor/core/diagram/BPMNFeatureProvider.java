package org.jboss.bpmn2.editor.core.diagram;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;
import org.jboss.bpmn2.editor.core.features.FeatureContainer;
import org.jboss.bpmn2.editor.core.features.FeatureResolver;
import org.jboss.bpmn2.editor.core.features.artifact.ArtifactFeatureResolver;
import org.jboss.bpmn2.editor.core.features.event.EventFeatureResolver;
import org.jboss.bpmn2.editor.core.features.event.definitions.CancelEventDefinitionContainer;
import org.jboss.bpmn2.editor.core.features.event.definitions.CompensateEventDefinitionContainer;
import org.jboss.bpmn2.editor.core.features.event.definitions.ConditionalEventDefinitionContainer;
import org.jboss.bpmn2.editor.core.features.event.definitions.ErrorEventDefinitionContainer;
import org.jboss.bpmn2.editor.core.features.event.definitions.EscalationEventDefinitionContainer;
import org.jboss.bpmn2.editor.core.features.event.definitions.LinkEventDefinitionContainer;
import org.jboss.bpmn2.editor.core.features.event.definitions.MessageEventDefinitionContainer;
import org.jboss.bpmn2.editor.core.features.event.definitions.SignalEventDefinitionContainer;
import org.jboss.bpmn2.editor.core.features.event.definitions.TimerEventDefinitionContainer;
import org.jboss.bpmn2.editor.core.features.flow.FlowFeatureResolver;
import org.jboss.bpmn2.editor.core.features.gateway.GatewayFeatureResolver;
import org.jboss.bpmn2.editor.core.features.lane.LaneFeatureResolver;
import org.jboss.bpmn2.editor.core.features.participant.ParticipantFeatureResolver;
import org.jboss.bpmn2.editor.core.features.task.TaskFeatureResolver;

/**
 * Determines what kinds of business objects can be added to a diagram.
 * 
 * @author imeikas
 * 
 */
public class BPMNFeatureProvider extends DefaultFeatureProvider {

	private List<FeatureContainer> containers;

	private List<FeatureResolver> resolvers;

	private ICreateFeature[] createFeatures;

	private ICreateConnectionFeature[] createConnectionFeatures;

	public BPMNFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);
		
		// TODO convert resolvers to containers, provides better decoupling
		resolvers = new ArrayList<FeatureResolver>();
		resolvers.add(new TaskFeatureResolver());
		resolvers.add(new EventFeatureResolver());
		resolvers.add(new GatewayFeatureResolver());
		resolvers.add(new FlowFeatureResolver());
		resolvers.add(new LaneFeatureResolver());
		resolvers.add(new ParticipantFeatureResolver());
		resolvers.add(new ArtifactFeatureResolver());

		containers = new ArrayList<FeatureContainer>();
		containers.add(new ConditionalEventDefinitionContainer());
		containers.add(new MessageEventDefinitionContainer());
		containers.add(new TimerEventDefinitionContainer());
		containers.add(new SignalEventDefinitionContainer());
		containers.add(new EscalationEventDefinitionContainer());
		containers.add(new CompensateEventDefinitionContainer());
		containers.add(new LinkEventDefinitionContainer());
		containers.add(new ErrorEventDefinitionContainer());
		containers.add(new CancelEventDefinitionContainer());
		
		List<ICreateFeature> createFeaturesList = new ArrayList<ICreateFeature>();
		
		for (FeatureResolver r : resolvers) {
			createFeaturesList.addAll(r.getCreateFeatures(this));
		}

		for(FeatureContainer container : containers) {
			createFeaturesList.add(container.getCreateFeature(this));
		}
		
		createFeatures = createFeaturesList.toArray(new ICreateFeature[createFeaturesList.size()]);

		List<ICreateConnectionFeature> createConnectionFeatureList = new ArrayList<ICreateConnectionFeature>();
		for (FeatureResolver r : resolvers) {
			createConnectionFeatureList.addAll(r.getCreateConnectionFeatures(this));
		}
		
		createConnectionFeatures = createConnectionFeatureList
		        .toArray(new ICreateConnectionFeature[createConnectionFeatureList.size()]);

	}

	@Override
	public IAddFeature getAddFeature(IAddContext context) {
		Object o = context.getNewObject();

		if (isNotBaseElement(o)) {
			return super.getAddFeature(context);
		}

		BaseElement element = (BaseElement) o;

		for (FeatureResolver r : resolvers) {
			IAddFeature f = r.getAddFeature(this, element);
			if (f != null) {
				return f;
			}
		}

		for (FeatureContainer container : containers) {
			if (container.canApplyTo(element)) {
				IAddFeature feature = container.getAddFeature(this);
				if (feature == null)
					continue;
				return feature;
			}
		}

		return super.getAddFeature(context);
	}

	@Override
	public ICreateFeature[] getCreateFeatures() {
		return createFeatures;
	}

	@Override
	public IUpdateFeature getUpdateFeature(IUpdateContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();
		if (pictogramElement instanceof ContainerShape) {
			Object o = getBusinessObjectForPictogramElement(pictogramElement);
			if (o instanceof BaseElement) {
				for (FeatureResolver r : resolvers) {
					IUpdateFeature f = r.getUpdateFeature(this, (BaseElement) o);
					if (f != null) {
						return f;
					}
				}
			}
		}
		return super.getUpdateFeature(context);
	}

	@Override
	public ICreateConnectionFeature[] getCreateConnectionFeatures() {
		return createConnectionFeatures;
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		Object o = getBusinessObjectForPictogramElement(pe);
		if (o instanceof BaseElement) {
			for (FeatureResolver r : resolvers) {
				IDirectEditingFeature f = r.getDirectEditingFeature(this, (BaseElement) o);
				if (f != null) {
					return f;
				}
			}
		}
		return super.getDirectEditingFeature(context);
	}

	@Override
	public ILayoutFeature getLayoutFeature(ILayoutContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();
		Object o = getBusinessObjectForPictogramElement(pictogramElement);
		if (o instanceof BaseElement) {
			for (FeatureResolver r : resolvers) {
				ILayoutFeature f = r.getLayoutFeature(this, (BaseElement) o);
				if (f != null) {
					return f;
				}
			}
		}
		return super.getLayoutFeature(context);
	}

	@Override
	public IMoveShapeFeature getMoveShapeFeature(IMoveShapeContext context) {
		Object o = getBusinessObjectForPictogramElement(context.getShape());
		if (o instanceof BaseElement) {
			for (FeatureResolver r : resolvers) {
				IMoveShapeFeature f = r.getMoveFeature(this, (BaseElement) o);
				if (f != null) {
					return f;
				}
			}
		}
		return super.getMoveShapeFeature(context);
	}

	@Override
	public IResizeShapeFeature getResizeShapeFeature(IResizeShapeContext context) {
		Object o = getBusinessObjectForPictogramElement(context.getShape());
		if (o instanceof BaseElement) {
			for (FeatureResolver r : resolvers) {
				IResizeShapeFeature f = r.getResizeFeature(this, (BaseElement) o);
				if (f != null) {
					return f;
				}
			}
		}
		return super.getResizeShapeFeature(context);
	}

	private boolean isNotBaseElement(Object o) {
		return !(o instanceof BaseElement);
	}
}
