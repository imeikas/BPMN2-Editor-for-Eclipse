package org.jboss.bpmn2.editor.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeature;
import org.jboss.bpmn2.editor.core.features.artifact.CreateAssociationFeature;
import org.jboss.bpmn2.editor.core.features.artifact.CreateTextAnnotationFeature;
import org.jboss.bpmn2.editor.core.features.event.BoundaryEventFeatureContainer.CreateBoundaryEvent;
import org.jboss.bpmn2.editor.core.features.event.CreateEndEventFeature;
import org.jboss.bpmn2.editor.core.features.event.CreateIntermediateCatchEventFeature;
import org.jboss.bpmn2.editor.core.features.event.CreateIntermediateThrowEventFeature;
import org.jboss.bpmn2.editor.core.features.event.CreateStartEventFeature;
import org.jboss.bpmn2.editor.core.features.event.definitions.CancelEventDefinitionContainer.CreateCancelEventDefinition;
import org.jboss.bpmn2.editor.core.features.event.definitions.CompensateEventDefinitionContainer.CreateCompensateEventDefinition;
import org.jboss.bpmn2.editor.core.features.event.definitions.ConditionalEventDefinitionContainer.CreateConditionalEventDefinition;
import org.jboss.bpmn2.editor.core.features.event.definitions.ErrorEventDefinitionContainer.CreateErrorEventDefinition;
import org.jboss.bpmn2.editor.core.features.event.definitions.EscalationEventDefinitionContainer.CreateEscalationEventDefinition;
import org.jboss.bpmn2.editor.core.features.event.definitions.LinkEventDefinitionContainer.CreateLinkEventDefinition;
import org.jboss.bpmn2.editor.core.features.event.definitions.MessageEventDefinitionContainer.CreateMessageEventDefinition;
import org.jboss.bpmn2.editor.core.features.event.definitions.SignalEventDefinitionContainer.CreateSignalEventDefinition;
import org.jboss.bpmn2.editor.core.features.event.definitions.TimerEventDefinitionContainer.CreateTimerEventDefinition;
import org.jboss.bpmn2.editor.core.features.flow.CreateMessageFlowFeature;
import org.jboss.bpmn2.editor.core.features.flow.CreateSequenceFlowFeature;
import org.jboss.bpmn2.editor.core.features.gateway.ComplexGatewayFeatureContainer.CreateComplexGatewayFeature;
import org.jboss.bpmn2.editor.core.features.gateway.EventBasedGatewayFeatureContainer.CreateEventBasedGatewayFeature;
import org.jboss.bpmn2.editor.core.features.gateway.ExclusiveGatewayFeatureContainer.CreateExclusiveGatewayFeature;
import org.jboss.bpmn2.editor.core.features.gateway.InclusiveGatewayFeatureContainer.CreateInclusiveGatewayFeature;
import org.jboss.bpmn2.editor.core.features.gateway.ParallelGatewayFeatureContainer.CreateParallelGatewayFeature;
import org.jboss.bpmn2.editor.core.features.lane.CreateLaneFeature;
import org.jboss.bpmn2.editor.core.features.participant.CreateParticipantFeature;
import org.jboss.bpmn2.editor.core.features.subprocess.SubProcessFeatureContainer.CreateSubProcessFeature;
import org.jboss.bpmn2.editor.core.features.subprocess.TransactionFeatureContainer.CreateTransactionFeature;
import org.jboss.bpmn2.editor.core.features.task.BusinessRuleTaskFeatureContainer.CreateBusinessRuleTaskFeature;
import org.jboss.bpmn2.editor.core.features.task.ManualTaskFeatureContainer.CreateManualTaskFeature;
import org.jboss.bpmn2.editor.core.features.task.ReceiveTaskFeatureContainer.CreateReceiveTaskFeature;
import org.jboss.bpmn2.editor.core.features.task.ScriptTaskFeatureContainer.CreateScriptTaskFeature;
import org.jboss.bpmn2.editor.core.features.task.SendTaskFeatureContainer.CreateSendTaskFeature;
import org.jboss.bpmn2.editor.core.features.task.ServiceTaskFeatureContainer.CreateServiceTaskFeature;
import org.jboss.bpmn2.editor.core.features.task.TaskFeatureContainer.CreateTaskFeature;
import org.jboss.bpmn2.editor.core.features.task.UserTaskFeatureContainer.CreateUserTaskFeature;

public class FeatureMap {
	public static Map<EClass, Class<? extends IFeature>> FEATURE_MAP;
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
		featureMap.put(i.getBoundaryEvent(), CreateBoundaryEvent.class);
		featureMap.put(i.getSubProcess(), CreateSubProcessFeature.class);
		featureMap.put(i.getTransaction(), CreateTransactionFeature.class);
		FEATURE_MAP = Collections.unmodifiableMap(featureMap);
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
