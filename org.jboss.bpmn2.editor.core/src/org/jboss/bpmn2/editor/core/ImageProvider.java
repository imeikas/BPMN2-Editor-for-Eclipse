package org.jboss.bpmn2.editor.core;

import org.eclipse.graphiti.ui.platform.AbstractImageProvider;

public class ImageProvider extends AbstractImageProvider {
	
	private static final String ICONS_16 = "icons/16/";
	
	private static final String PREFIX = "org.jboss.bpmn2.editor.core.";
	
	public static final String IMG_16_START_EVENT = PREFIX + "startevent.16"; 
	public static final String IMG_16_END_EVENT = PREFIX + "endevent.16";
	public static final String IMG_16_TASK = PREFIX + "Task.png"; 
	public static final String IMG_16_EXCLUSIVE_GATEWAY = PREFIX + "exclusivegateway.16";
	public static final String IMG_16_SEQUENCE_FLOW = PREFIX + "sequenceflow.16";
	
	@Override
	protected void addAvailableImages() {
		addImageFilePath(IMG_16_START_EVENT, ICONS_16 + "StartEvent.png");
		addImageFilePath(IMG_16_END_EVENT, ICONS_16 + "EndEvent.png");
		addImageFilePath(IMG_16_TASK, ICONS_16 + "Task.png");
		addImageFilePath(IMG_16_EXCLUSIVE_GATEWAY, ICONS_16 + "ExclusiveGateway.png");
		addImageFilePath(IMG_16_SEQUENCE_FLOW, ICONS_16 + "SequenceFlow.png");
	}
}
