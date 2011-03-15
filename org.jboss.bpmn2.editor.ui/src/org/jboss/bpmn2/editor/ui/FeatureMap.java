/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.bpmn2.editor.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeature;
import org.jboss.bpmn2.editor.ui.features.activity.subprocess.AdHocSubProcessFeatureContainer.CreateAdHocSubProcessFeature;
import org.jboss.bpmn2.editor.ui.features.activity.subprocess.CallActivityFeatureContainer.CreateCallActivityFeatureContainer;
import org.jboss.bpmn2.editor.ui.features.activity.subprocess.SubProcessFeatureContainer.CreateSubProcessFeature;
import org.jboss.bpmn2.editor.ui.features.activity.subprocess.TransactionFeatureContainer.CreateTransactionFeature;
import org.jboss.bpmn2.editor.ui.features.activity.task.BusinessRuleTaskFeatureContainer.CreateBusinessRuleTaskFeature;
import org.jboss.bpmn2.editor.ui.features.activity.task.ManualTaskFeatureContainer.CreateManualTaskFeature;
import org.jboss.bpmn2.editor.ui.features.activity.task.ReceiveTaskFeatureContainer.CreateReceiveTaskFeature;
import org.jboss.bpmn2.editor.ui.features.activity.task.ScriptTaskFeatureContainer.CreateScriptTaskFeature;
import org.jboss.bpmn2.editor.ui.features.activity.task.SendTaskFeatureContainer.CreateSendTaskFeature;
import org.jboss.bpmn2.editor.ui.features.activity.task.ServiceTaskFeatureContainer.CreateServiceTaskFeature;
import org.jboss.bpmn2.editor.ui.features.activity.task.TaskFeatureContainer.CreateTaskFeature;
import org.jboss.bpmn2.editor.ui.features.activity.task.UserTaskFeatureContainer.CreateUserTaskFeature;
import org.jboss.bpmn2.editor.ui.features.artifact.CreateTextAnnotationFeature;
import org.jboss.bpmn2.editor.ui.features.artifact.GroupFeatureContainer.CreateGroupFeature;
import org.jboss.bpmn2.editor.ui.features.choreography.CallChoreographyFeatureContainer.CreateCallChoreographyFeature;
import org.jboss.bpmn2.editor.ui.features.choreography.ChoreographyTaskFeatureContainer.CreateChoreographyTaskFeature;
import org.jboss.bpmn2.editor.ui.features.choreography.SubChoreographyFeatureContainer.CreateSubChoreographyFeature;
import org.jboss.bpmn2.editor.ui.features.conversation.ConversationLinkFeatureContainer.CreateConversationLinkFeature;
import org.jboss.bpmn2.editor.ui.features.conversation.CreateConversationFeature;
import org.jboss.bpmn2.editor.ui.features.data.DataInputFeatureContainer.CreateDataInputFeature;
import org.jboss.bpmn2.editor.ui.features.data.DataObjectFeatureContainer.CreateDataObjectFeature;
import org.jboss.bpmn2.editor.ui.features.data.DataObjectReferenceFeatureContainer.CreateDataObjectReferenceFeature;
import org.jboss.bpmn2.editor.ui.features.data.DataOutputFeatureContainer.CreateDataOutputFeature;
import org.jboss.bpmn2.editor.ui.features.data.DataStoreReferenceFeatureContainer.CreateDataStoreReferenceFeature;
import org.jboss.bpmn2.editor.ui.features.data.MessageFeatureContainer.CreateMessageFeature;
import org.jboss.bpmn2.editor.ui.features.event.CreateBoundaryEventFeature;
import org.jboss.bpmn2.editor.ui.features.event.EndEventFeatureContainer.CreateEndEventFeature;
import org.jboss.bpmn2.editor.ui.features.event.IntermediateCatchEventFeatureContainer.CreateIntermediateCatchEventFeature;
import org.jboss.bpmn2.editor.ui.features.event.IntermediateThrowEventFeatureContainer.CreateIntermediateThrowEventFeature;
import org.jboss.bpmn2.editor.ui.features.event.StartEventFeatureContainer.CreateStartEventFeature;
import org.jboss.bpmn2.editor.ui.features.event.definitions.CancelEventDefinitionContainer.CreateCancelEventDefinition;
import org.jboss.bpmn2.editor.ui.features.event.definitions.CompensateEventDefinitionContainer.CreateCompensateEventDefinition;
import org.jboss.bpmn2.editor.ui.features.event.definitions.ConditionalEventDefinitionContainer.CreateConditionalEventDefinition;
import org.jboss.bpmn2.editor.ui.features.event.definitions.ErrorEventDefinitionContainer.CreateErrorEventDefinition;
import org.jboss.bpmn2.editor.ui.features.event.definitions.EscalationEventDefinitionContainer.CreateEscalationEventDefinition;
import org.jboss.bpmn2.editor.ui.features.event.definitions.LinkEventDefinitionContainer.CreateLinkEventDefinition;
import org.jboss.bpmn2.editor.ui.features.event.definitions.MessageEventDefinitionContainer.CreateMessageEventDefinition;
import org.jboss.bpmn2.editor.ui.features.event.definitions.SignalEventDefinitionContainer.CreateSignalEventDefinition;
import org.jboss.bpmn2.editor.ui.features.event.definitions.TerminateEventDefinitionFeatureContainer.CreateTerminateEventDefinition;
import org.jboss.bpmn2.editor.ui.features.event.definitions.TimerEventDefinitionContainer.CreateTimerEventDefinition;
import org.jboss.bpmn2.editor.ui.features.flow.AssociationFeatureContainer.CreateAssociationFeature;
import org.jboss.bpmn2.editor.ui.features.flow.MessageFlowFeatureContainer.CreateMessageFlowFeature;
import org.jboss.bpmn2.editor.ui.features.flow.SequenceFlowFeatureContainer.CreateSequenceFlowFeature;
import org.jboss.bpmn2.editor.ui.features.gateway.ComplexGatewayFeatureContainer.CreateComplexGatewayFeature;
import org.jboss.bpmn2.editor.ui.features.gateway.EventBasedGatewayFeatureContainer.CreateEventBasedGatewayFeature;
import org.jboss.bpmn2.editor.ui.features.gateway.ExclusiveGatewayFeatureContainer.CreateExclusiveGatewayFeature;
import org.jboss.bpmn2.editor.ui.features.gateway.InclusiveGatewayFeatureContainer.CreateInclusiveGatewayFeature;
import org.jboss.bpmn2.editor.ui.features.gateway.ParallelGatewayFeatureContainer.CreateParallelGatewayFeature;
import org.jboss.bpmn2.editor.ui.features.lane.CreateLaneFeature;
import org.jboss.bpmn2.editor.ui.features.participant.CreateParticipantFeature;

public class FeatureMap {
	public static final Map<EClass, Class<? extends IFeature>> FEATURE_MAP;
	public static final List<Class<? extends IFeature>> EVENT_DEFINITIONS;
	public static final List<Class<? extends IFeature>> EVENTS;
	public static final List<Class<? extends IFeature>> GATEWAYS;
	public static final List<Class<? extends IFeature>> TASKS;
	public static final List<Class<? extends IFeature>> DATA;
	public static final List<Class<? extends IFeature>> OTHER;
	static {
		Bpmn2Package i = Bpmn2Package.eINSTANCE;

		HashMap<EClass, Class<? extends IFeature>> featureMap = new HashMap<EClass, Class<? extends IFeature>>();
		featureMap.put(i.getTask(), CreateTaskFeature.class);
		featureMap.put(i.getStartEvent(), CreateStartEventFeature.class);
		featureMap.put(i.getEndEvent(), CreateEndEventFeature.class);
		featureMap.put(i.getSequenceFlow(), CreateSequenceFlowFeature.class);
		featureMap.put(i.getInclusiveGateway(), CreateInclusiveGatewayFeature.class);
		featureMap.put(i.getExclusiveGateway(), CreateExclusiveGatewayFeature.class);
		featureMap.put(i.getParallelGateway(), CreateParallelGatewayFeature.class);
		featureMap.put(i.getLane(), CreateLaneFeature.class);
		featureMap.put(i.getCollaboration(), CreateParticipantFeature.class);
		featureMap.put(i.getAssociation(), CreateAssociationFeature.class);
		featureMap.put(i.getTextAnnotation(), CreateTextAnnotationFeature.class);
		featureMap.put(i.getEventBasedGateway(), CreateEventBasedGatewayFeature.class);
		featureMap.put(i.getComplexGateway(), CreateComplexGatewayFeature.class);
		featureMap.put(i.getMessageFlow(), CreateMessageFlowFeature.class);
		featureMap.put(i.getIntermediateThrowEvent(), CreateIntermediateThrowEventFeature.class);
		featureMap.put(i.getIntermediateCatchEvent(), CreateIntermediateCatchEventFeature.class);
		featureMap.put(i.getManualTask(), CreateManualTaskFeature.class);
		featureMap.put(i.getUserTask(), CreateUserTaskFeature.class);
		featureMap.put(i.getScriptTask(), CreateScriptTaskFeature.class);
		featureMap.put(i.getBusinessRuleTask(), CreateBusinessRuleTaskFeature.class);
		featureMap.put(i.getServiceTask(), CreateServiceTaskFeature.class);
		featureMap.put(i.getSendTask(), CreateSendTaskFeature.class);
		featureMap.put(i.getReceiveTask(), CreateReceiveTaskFeature.class);
		featureMap.put(i.getConditionalEventDefinition(), CreateConditionalEventDefinition.class);
		featureMap.put(i.getTimerEventDefinition(), CreateTimerEventDefinition.class);
		featureMap.put(i.getSignalEventDefinition(), CreateSignalEventDefinition.class);
		featureMap.put(i.getMessageEventDefinition(), CreateMessageEventDefinition.class);
		featureMap.put(i.getEscalationEventDefinition(), CreateEscalationEventDefinition.class);
		featureMap.put(i.getCompensateEventDefinition(), CreateCompensateEventDefinition.class);
		featureMap.put(i.getLinkEventDefinition(), CreateLinkEventDefinition.class);
		featureMap.put(i.getErrorEventDefinition(), CreateErrorEventDefinition.class);
		featureMap.put(i.getCancelEventDefinition(), CreateCancelEventDefinition.class);
		featureMap.put(i.getTerminateEventDefinition(), CreateTerminateEventDefinition.class);
		featureMap.put(i.getBoundaryEvent(), CreateBoundaryEventFeature.class);
		featureMap.put(i.getSubProcess(), CreateSubProcessFeature.class);
		featureMap.put(i.getTransaction(), CreateTransactionFeature.class);
		featureMap.put(i.getGroup(), CreateGroupFeature.class);
		featureMap.put(i.getDataObject(), CreateDataObjectFeature.class);
		featureMap.put(i.getDataObjectReference(), CreateDataObjectReferenceFeature.class);
		featureMap.put(i.getDataStoreReference(), CreateDataStoreReferenceFeature.class);
		featureMap.put(i.getDataInput(), CreateDataInputFeature.class);
		featureMap.put(i.getDataOutput(), CreateDataOutputFeature.class);
		featureMap.put(i.getAdHocSubProcess(), CreateAdHocSubProcessFeature.class);
		featureMap.put(i.getCallActivity(), CreateCallActivityFeatureContainer.class);
		featureMap.put(i.getMessage(), CreateMessageFeature.class);
		featureMap.put(i.getConversation(), CreateConversationFeature.class);
		featureMap.put(i.getConversationLink(), CreateConversationLinkFeature.class);
		featureMap.put(i.getChoreographyTask(), CreateChoreographyTaskFeature.class);
		featureMap.put(i.getSubChoreography(), CreateSubChoreographyFeature.class);
		featureMap.put(i.getCallChoreography(), CreateCallChoreographyFeature.class);
		FEATURE_MAP = Collections.unmodifiableMap(featureMap);

		ArrayList<Class<? extends IFeature>> features = new ArrayList<Class<? extends IFeature>>();
		features.add(CreateConditionalEventDefinition.class);
		features.add(CreateTimerEventDefinition.class);
		features.add(CreateSignalEventDefinition.class);
		features.add(CreateMessageEventDefinition.class);
		features.add(CreateEscalationEventDefinition.class);
		features.add(CreateCompensateEventDefinition.class);
		features.add(CreateLinkEventDefinition.class);
		features.add(CreateErrorEventDefinition.class);
		features.add(CreateCancelEventDefinition.class);
		features.add(CreateTerminateEventDefinition.class);
		EVENT_DEFINITIONS = Collections.unmodifiableList(features);

		features = new ArrayList<Class<? extends IFeature>>();
		features.add(CreateStartEventFeature.class);
		features.add(CreateEndEventFeature.class);
		features.add(CreateIntermediateThrowEventFeature.class);
		features.add(CreateIntermediateCatchEventFeature.class);
		features.add(CreateBoundaryEventFeature.class);
		EVENTS = Collections.unmodifiableList(features);

		features = new ArrayList<Class<? extends IFeature>>();
		features.add(CreateInclusiveGatewayFeature.class);
		features.add(CreateExclusiveGatewayFeature.class);
		features.add(CreateParallelGatewayFeature.class);
		features.add(CreateEventBasedGatewayFeature.class);
		features.add(CreateComplexGatewayFeature.class);

		GATEWAYS = Collections.unmodifiableList(features);

		features = new ArrayList<Class<? extends IFeature>>();
		features.add(CreateTaskFeature.class);
		features.add(CreateManualTaskFeature.class);
		features.add(CreateUserTaskFeature.class);
		features.add(CreateScriptTaskFeature.class);
		features.add(CreateBusinessRuleTaskFeature.class);
		features.add(CreateServiceTaskFeature.class);
		features.add(CreateSendTaskFeature.class);
		features.add(CreateReceiveTaskFeature.class);
		features.add(CreateChoreographyTaskFeature.class);
		TASKS = Collections.unmodifiableList(features);

		features = new ArrayList<Class<? extends IFeature>>();
		features.add(CreateDataObjectFeature.class);
		features.add(CreateDataObjectReferenceFeature.class);
		features.add(CreateDataStoreReferenceFeature.class);
		features.add(CreateDataInputFeature.class);
		features.add(CreateDataOutputFeature.class);
		DATA = Collections.unmodifiableList(features);

		features = new ArrayList<Class<? extends IFeature>>();
		features.add(CreateLaneFeature.class);
		features.add(CreateParticipantFeature.class);
		features.add(CreateTextAnnotationFeature.class);
		features.add(CreateSubProcessFeature.class);
		features.add(CreateTransactionFeature.class);
		features.add(CreateGroupFeature.class);
		features.add(CreateAdHocSubProcessFeature.class);
		features.add(CreateCallActivityFeatureContainer.class);
		features.add(CreateMessageFeature.class);
		features.add(CreateConversationFeature.class);
		features.add(CreateSubChoreographyFeature.class);
		features.add(CreateCallChoreographyFeature.class);
		OTHER = Collections.unmodifiableList(features);

	}

	public static EClass getElement(IFeature cf) {
		for (EClass e : FEATURE_MAP.keySet()) {
			if (FEATURE_MAP.get(e).isInstance(cf)) {
				return e;
			}
		}
		return null;
	}
}
