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
package org.eclipse.bpmn2.modeler.ui.diagram;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.modeler.core.features.ConnectionFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.DefaultBpmnDeleteFeature;
import org.eclipse.bpmn2.modeler.core.features.FeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.bendpoint.AddBendpointFeature;
import org.eclipse.bpmn2.modeler.core.features.bendpoint.MoveBendpointFeature;
import org.eclipse.bpmn2.modeler.core.features.bendpoint.RemoveBendpointFeature;
import org.eclipse.bpmn2.modeler.ui.features.activity.subprocess.AdHocSubProcessFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.subprocess.CallActivityFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.subprocess.SubProcessFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.subprocess.TransactionFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.BusinessRuleTaskFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.CustomTaskFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.ManualTaskFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.ReceiveTaskFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.ScriptTaskFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.SendTaskFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.ServiceTaskFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.TaskFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.UserTaskFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.artifact.GroupFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.artifact.TextAnnotationFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.choreography.CallChoreographyFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.choreography.ChoreographyMessageLinkFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.choreography.ChoreographyTaskFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.choreography.SubChoreographyFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.conversation.ConversationFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.conversation.ConversationLinkFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.data.DataInputFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.data.DataObjectFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.data.DataObjectReferenceFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.data.DataOutputFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.data.DataStoreReferenceFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.data.MessageFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.event.BoundaryEventFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.event.EndEventFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.event.IntermediateCatchEventFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.event.IntermediateThrowEventFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.event.StartEventFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.event.definitions.CancelEventDefinitionContainer;
import org.eclipse.bpmn2.modeler.ui.features.event.definitions.CompensateEventDefinitionContainer;
import org.eclipse.bpmn2.modeler.ui.features.event.definitions.ConditionalEventDefinitionContainer;
import org.eclipse.bpmn2.modeler.ui.features.event.definitions.ErrorEventDefinitionContainer;
import org.eclipse.bpmn2.modeler.ui.features.event.definitions.EscalationEventDefinitionContainer;
import org.eclipse.bpmn2.modeler.ui.features.event.definitions.LinkEventDefinitionContainer;
import org.eclipse.bpmn2.modeler.ui.features.event.definitions.MessageEventDefinitionContainer;
import org.eclipse.bpmn2.modeler.ui.features.event.definitions.SignalEventDefinitionContainer;
import org.eclipse.bpmn2.modeler.ui.features.event.definitions.TerminateEventDefinitionFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.event.definitions.TimerEventDefinitionContainer;
import org.eclipse.bpmn2.modeler.ui.features.flow.AssociationFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.flow.DataInputAssociationFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.flow.DataOutputAssociationFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.flow.MessageFlowFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.flow.SequenceFlowFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.gateway.ComplexGatewayFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.gateway.EventBasedGatewayFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.gateway.ExclusiveGatewayFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.gateway.InclusiveGatewayFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.gateway.ParallelGatewayFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.lane.LaneFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.participant.ParticipantFeatureContainer;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddBendpointFeature;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IMoveBendpointFeature;
import org.eclipse.graphiti.features.IMoveShapeFeature;
import org.eclipse.graphiti.features.IReconnectionFeature;
import org.eclipse.graphiti.features.IRemoveBendpointFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddBendpointContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IMoveBendpointContext;
import org.eclipse.graphiti.features.context.IMoveShapeContext;
import org.eclipse.graphiti.features.context.IReconnectionContext;
import org.eclipse.graphiti.features.context.IRemoveBendpointContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;

/**
 * Determines what kinds of business objects can be added to a diagram.
 * 
 * @author imeikas
 * 
 */
public class BPMNFeatureProvider extends DefaultFeatureProvider {

	private List<FeatureContainer> containers;

	private ICreateFeature[] createFeatures;

	private ICreateConnectionFeature[] createConnectionFeatures;

	public BPMNFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);

		containers = new ArrayList<FeatureContainer>();
		containers.add(new GroupFeatureContainer());
		containers.add(new DataObjectFeatureContainer());
		containers.add(new DataObjectReferenceFeatureContainer());
		containers.add(new DataStoreReferenceFeatureContainer());
		containers.add(new DataInputFeatureContainer());
		containers.add(new DataOutputFeatureContainer());
		containers.add(new MessageFeatureContainer());
		containers.add(new StartEventFeatureContainer());
		containers.add(new EndEventFeatureContainer());
		containers.add(new IntermediateCatchEventFeatureContainer());
		containers.add(new IntermediateThrowEventFeatureContainer());
		containers.add(new BoundaryEventFeatureContainer());
		containers.add(new TaskFeatureContainer());
		containers.add(new ScriptTaskFeatureContainer());
		containers.add(new ServiceTaskFeatureContainer());
		containers.add(new UserTaskFeatureContainer());
		containers.add(new ManualTaskFeatureContainer());
		containers.add(new BusinessRuleTaskFeatureContainer());
		containers.add(new SendTaskFeatureContainer());
		containers.add(new ReceiveTaskFeatureContainer());
		containers.add(new ChoreographyTaskFeatureContainer());
		containers.add(new ExclusiveGatewayFeatureContainer());
		containers.add(new InclusiveGatewayFeatureContainer());
		containers.add(new ParallelGatewayFeatureContainer());
		containers.add(new EventBasedGatewayFeatureContainer());
		containers.add(new ComplexGatewayFeatureContainer());
		containers.add(new AdHocSubProcessFeatureContainer());
		containers.add(new CallActivityFeatureContainer());
		containers.add(new TransactionFeatureContainer());
		containers.add(new SubProcessFeatureContainer());
		containers.add(new ConditionalEventDefinitionContainer());
		containers.add(new MessageEventDefinitionContainer());
		containers.add(new TimerEventDefinitionContainer());
		containers.add(new SignalEventDefinitionContainer());
		containers.add(new EscalationEventDefinitionContainer());
		containers.add(new CompensateEventDefinitionContainer());
		containers.add(new LinkEventDefinitionContainer());
		containers.add(new ErrorEventDefinitionContainer());
		containers.add(new CancelEventDefinitionContainer());
		containers.add(new TerminateEventDefinitionFeatureContainer());
		containers.add(new SequenceFlowFeatureContainer());
		containers.add(new MessageFlowFeatureContainer());
		containers.add(new AssociationFeatureContainer());
		containers.add(new ConversationFeatureContainer());
		containers.add(new ConversationLinkFeatureContainer());
		containers.add(new DataInputAssociationFeatureContainer());
		containers.add(new DataOutputAssociationFeatureContainer());
		containers.add(new SubChoreographyFeatureContainer());
		containers.add(new CallChoreographyFeatureContainer());
		containers.add(new ParticipantFeatureContainer());
		containers.add(new LaneFeatureContainer());
		containers.add(new TextAnnotationFeatureContainer());
		containers.add(new ChoreographyMessageLinkFeatureContainer());

		updateFeatureLists();
	}

	public void addFeatureContainer(FeatureContainer fc) throws Exception {
		
		boolean canAdd = true;
		
		if (fc instanceof CustomTaskFeatureContainer) {
			CustomTaskFeatureContainer ctfc = (CustomTaskFeatureContainer)fc;
			for (FeatureContainer container : containers) {
				if (container instanceof CustomTaskFeatureContainer) {
					// don't add duplicates
					String oldId = ((CustomTaskFeatureContainer)container).getId();
					String newId = ctfc.getId();
					if (oldId!=null && newId!=null) {
						if (oldId.equals(newId)) {
							canAdd = false;
							break;
						}
					}
					else if (oldId==newId) {
						canAdd = false;
						break;
					}
				}
			}
		}
		else {
			for (FeatureContainer container : containers) {
				if (container.getClass().isInstance(fc)) {
					canAdd = false;
					break;
				}
			}
		}
		if (canAdd) {
			containers.add(fc);
			updateFeatureLists();
		}
		else
			throw new Exception("Attempt to add a Custom Feature with a duplicate ID "+fc.getClass().getName());
	}
	
	private void updateFeatureLists() {
		
		List<ICreateFeature> createFeaturesList = new ArrayList<ICreateFeature>();

		for (FeatureContainer container : containers) {
			ICreateFeature createFeature = container.getCreateFeature(this);
			if (createFeature != null) {
				createFeaturesList.add(createFeature);
			}
		}

		createFeatures = createFeaturesList.toArray(new ICreateFeature[createFeaturesList.size()]);

		List<ICreateConnectionFeature> createConnectionFeatureList = new ArrayList<ICreateConnectionFeature>();

		for (FeatureContainer c : containers) {
			if (c instanceof ConnectionFeatureContainer) {
				ConnectionFeatureContainer connectionFeatureContainer = (ConnectionFeatureContainer) c;
				ICreateConnectionFeature createConnectionFeature = connectionFeatureContainer
						.getCreateConnectionFeature(this);
				if (createConnectionFeature == null) {
					continue;
				}
				createConnectionFeatureList.add(createConnectionFeature);
			}
		}

		createConnectionFeatures = createConnectionFeatureList
				.toArray(new ICreateConnectionFeature[createConnectionFeatureList.size()]);
	}
	
	@Override
	public IAddFeature getAddFeature(IAddContext context) {
		Object id = CustomTaskFeatureContainer.getId(context); 
		for (FeatureContainer container : containers) {
			if (id!=null && !(container instanceof CustomTaskFeatureContainer))
				continue;
			Object o = container.getApplyObject(context);
			if (o != null && container.canApplyTo(o)) {
				IAddFeature feature = container.getAddFeature(this);
				if (feature == null) {
					break;
				}
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
		Object id = CustomTaskFeatureContainer.getId(context); 
		for (FeatureContainer container : containers) {
			if (id!=null && !(container instanceof CustomTaskFeatureContainer))
				continue;
			Object o = container.getApplyObject(context);
			if (o != null && container.canApplyTo(o)) {
				IUpdateFeature feature = container.getUpdateFeature(this);
				if (feature == null) {
					break;
				}
				return feature;
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
		Object id = CustomTaskFeatureContainer.getId(context); 
		for (FeatureContainer container : containers) {
			if (id!=null && !(container instanceof CustomTaskFeatureContainer))
				continue;
			Object o = container.getApplyObject(context);
			if (o != null && container.canApplyTo(o)) {
				IDirectEditingFeature feature = container.getDirectEditingFeature(this);
				if (feature == null) {
					break;
				}
				return feature;
			}
		}

		return super.getDirectEditingFeature(context);
	}

	@Override
	public ILayoutFeature getLayoutFeature(ILayoutContext context) {
		Object id = CustomTaskFeatureContainer.getId(context); 
		for (FeatureContainer container : containers) {
			if (id!=null && !(container instanceof CustomTaskFeatureContainer))
				continue;
			Object o = container.getApplyObject(context);
			if (o != null && container.canApplyTo(o)) {
				ILayoutFeature feature = container.getLayoutFeature(this);
				if (feature == null) {
					break;
				}
				return feature;
			}
		}

		return super.getLayoutFeature(context);
	}

	@Override
	public IMoveShapeFeature getMoveShapeFeature(IMoveShapeContext context) {
		Object id = CustomTaskFeatureContainer.getId(context); 
		for (FeatureContainer container : containers) {
			if (id!=null && !(container instanceof CustomTaskFeatureContainer))
				continue;
			Object o = container.getApplyObject(context);
			if (o != null && container.canApplyTo(o)) {
				IMoveShapeFeature feature = container.getMoveFeature(this);
				if (feature == null) {
					break;
				}
				return feature;
			}
		}

		return super.getMoveShapeFeature(context);
	}

	@Override
	public IResizeShapeFeature getResizeShapeFeature(IResizeShapeContext context) {
		Object id = CustomTaskFeatureContainer.getId(context); 
		for (FeatureContainer container : containers) {
			if (id!=null && !(container instanceof CustomTaskFeatureContainer))
				continue;
			Object o = container.getApplyObject(context);
			if (o != null && container.canApplyTo(o)) {
				IResizeShapeFeature feature = container.getResizeFeature(this);
				if (feature == null) {
					break;
				}
				return feature;
			}
		}

		return super.getResizeShapeFeature(context);
	}

	@Override
	public IAddBendpointFeature getAddBendpointFeature(IAddBendpointContext context) {
		return new AddBendpointFeature(this);
	}

	@Override
	public IMoveBendpointFeature getMoveBendpointFeature(IMoveBendpointContext context) {
		return new MoveBendpointFeature(this);
	}

	@Override
	public IRemoveBendpointFeature getRemoveBendpointFeature(IRemoveBendpointContext context) {
		return new RemoveBendpointFeature(this);
	}

	@Override
	public IReconnectionFeature getReconnectionFeature(
			IReconnectionContext context) {
		for (FeatureContainer container : containers) {
			Object o = container.getApplyObject(context);
			if (o != null && container.canApplyTo(o) && container instanceof ConnectionFeatureContainer) {
				IReconnectionFeature feature = ((ConnectionFeatureContainer)container).getReconnectionFeature(this);
				if (feature == null) {
					break;
				}
				return feature;
			}
		}
		return super.getReconnectionFeature(context);
	}

	@Override
	public IDeleteFeature getDeleteFeature(IDeleteContext context) {
		Object id = CustomTaskFeatureContainer.getId(context); 
		for (FeatureContainer container : containers) {
			if (id!=null && !(container instanceof CustomTaskFeatureContainer))
				continue;
			Object o = container.getApplyObject(context);
			if (o != null && container.canApplyTo(o)) {
				IDeleteFeature feature = container.getDeleteFeature(this);
				if (feature != null) {
					return feature;
				}
			}
		}
		return new DefaultBpmnDeleteFeature(this);
	}

}