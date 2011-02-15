package org.jboss.bpmn2.editor.core;

import org.eclipse.bpmn2.Association;
import org.eclipse.bpmn2.ComplexGateway;
import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.EventBasedGateway;
import org.eclipse.bpmn2.ExclusiveGateway;
import org.eclipse.bpmn2.InclusiveGateway;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.bpmn2.Lane;
import org.eclipse.bpmn2.ManualTask;
import org.eclipse.bpmn2.MessageFlow;
import org.eclipse.bpmn2.ParallelGateway;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.bpmn2.UserTask;
import org.eclipse.graphiti.ui.platform.AbstractImageProvider;

public class ImageProvider extends AbstractImageProvider {
	
	private static final String dot16 = ".16";
	private static final String ICONS_16 = "icons/16/";
	
	public static final String PREFIX = ImageProvider.class.getPackage().getName() + ".";
	
	public static final String IMG_16_START_EVENT = PREFIX + StartEvent.class.getSimpleName().toLowerCase() + dot16;  
	public static final String IMG_16_END_EVENT = PREFIX + EndEvent.class.getSimpleName().toLowerCase() + dot16;
	public static final String IMG_16_TASK = PREFIX + Task.class.getSimpleName().toLowerCase() + dot16; 
	public static final String IMG_16_EXCLUSIVE_GATEWAY = PREFIX + ExclusiveGateway.class.getSimpleName().toLowerCase() + dot16;
	public static final String IMG_16_SEQUENCE_FLOW = PREFIX + SequenceFlow.class.getSimpleName().toLowerCase() + dot16;
	public static final String IMG_16_PARTICIPANT = PREFIX + Participant.class.getSimpleName().toLowerCase() + dot16;
	public static final String IMG_16_LANE = PREFIX + Lane.class.getSimpleName().toLowerCase() + dot16;
	public static final String IMG_16_TEXT_ANNOTATION = PREFIX + TextAnnotation.class.getSimpleName().toLowerCase() + dot16;
	public static final String IMG_16_ASSOCIATION = PREFIX + Association.class.getSimpleName().toLowerCase() + dot16;
	public static final String IMG_16_INCLUSIVE_GATEWAY = PREFIX + InclusiveGateway.class.getSimpleName().toLowerCase() + dot16;
	public static final String IMG_16_PARALLEL_GATEWAY = PREFIX + ParallelGateway.class.getSimpleName().toLowerCase() + dot16;
	public static final String IMG_16_EVENT_BASED_GATEWAY = PREFIX + EventBasedGateway.class.getSimpleName().toLowerCase() + dot16;
	public static final String IMG_16_MESSAGE_FLOW = PREFIX + MessageFlow.class.getSimpleName().toLowerCase() + dot16;
	public static final String IMG_16_INTERMEDIATE_CATCH_EVENT = PREFIX + IntermediateCatchEvent.class.getSimpleName().toLowerCase() + dot16;
	public static final String IMG_16_INTERMEDIATE_THORW_EVENT = PREFIX + IntermediateThrowEvent.class.getSimpleName().toLowerCase() + dot16;
	public static final String IMG_16_COMPLEX_GATEWAY = PREFIX + ComplexGateway.class.getSimpleName().toLowerCase() + dot16;
	public static final String IMG_16_USER_TASK = PREFIX + UserTask.class.getSimpleName().toLowerCase() + dot16;
	public static final String IMG_16_MANUAL_TASK = PREFIX + ManualTask.class.getSimpleName().toLowerCase() + dot16;
	
	@Override
	protected void addAvailableImages() {
		addImageFilePath(IMG_16_START_EVENT, ICONS_16 + "StartEvent.png");
		addImageFilePath(IMG_16_END_EVENT, ICONS_16 + "EndEvent.png");
		addImageFilePath(IMG_16_TASK, ICONS_16 + "Task.png");
		addImageFilePath(IMG_16_EXCLUSIVE_GATEWAY, ICONS_16 + "ExclusiveGateway.png");
		addImageFilePath(IMG_16_SEQUENCE_FLOW, ICONS_16 + "SequenceFlow.png");
		addImageFilePath(IMG_16_PARTICIPANT, ICONS_16 + "Participant.gif");
		addImageFilePath(IMG_16_LANE, ICONS_16 + "Lane.gif");
		addImageFilePath(IMG_16_TEXT_ANNOTATION, ICONS_16 + "TextAnnotation.png");
		addImageFilePath(IMG_16_ASSOCIATION, ICONS_16 + "Association.png");
		addImageFilePath(IMG_16_INCLUSIVE_GATEWAY, ICONS_16 + "InclusiveGateway.png");
		addImageFilePath(IMG_16_PARALLEL_GATEWAY, ICONS_16 + "InclusiveGateway.png");
		addImageFilePath(IMG_16_EVENT_BASED_GATEWAY, ICONS_16 + "EventBasedGateway.png");
		addImageFilePath(IMG_16_MESSAGE_FLOW, ICONS_16 + "MessageFlow.png");
		addImageFilePath(IMG_16_INTERMEDIATE_CATCH_EVENT, ICONS_16 + "IntermediateCatchEvent.png");
		addImageFilePath(IMG_16_INTERMEDIATE_THORW_EVENT, ICONS_16 + "IntermediateThrowEvent.png");
		addImageFilePath(IMG_16_COMPLEX_GATEWAY, ICONS_16 + "ComplexGateway.png");
		addImageFilePath(IMG_16_USER_TASK, ICONS_16 + "UserTask.png");
		addImageFilePath(IMG_16_MANUAL_TASK, ICONS_16 + "ManualTask.png");
	}
}
