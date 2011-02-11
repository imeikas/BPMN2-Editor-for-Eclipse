package org.jboss.bpmn2.editor.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeature;
import org.jboss.bpmn2.editor.core.features.artifact.CreateAssociationFeature;
import org.jboss.bpmn2.editor.core.features.artifact.CreateTextAnnotationFeature;
import org.jboss.bpmn2.editor.core.features.event.CreateEndEventFeature;
import org.jboss.bpmn2.editor.core.features.event.CreateStartEventFeature;
import org.jboss.bpmn2.editor.core.features.flow.CreateMessageFlowFeature;
import org.jboss.bpmn2.editor.core.features.flow.CreateSequenceFlowFeature;
import org.jboss.bpmn2.editor.core.features.gateway.CreateEventBasedGatewayFeature;
import org.jboss.bpmn2.editor.core.features.gateway.CreateExclusiveGatewayFeature;
import org.jboss.bpmn2.editor.core.features.gateway.CreateInclusiveGatewayFeature;
import org.jboss.bpmn2.editor.core.features.gateway.CreateParallelGatewayFeature;
import org.jboss.bpmn2.editor.core.features.lane.CreateLaneFeature;
import org.jboss.bpmn2.editor.core.features.participant.CreateParticipantFeature;
import org.jboss.bpmn2.editor.core.features.task.CreateTaskFeature;

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
		featureMap.put(i.getMessageFlow(), CreateMessageFlowFeature.class);
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
