package org.jboss.bpmn2.editor.core.features.event.definitions;

import java.util.List;

import org.eclipse.bpmn2.CatchEvent;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.ThrowEvent;

public class EventDefinitionSupport {
	
	interface EventWithDefinitions {
		List<EventDefinition> getEventDefinitions();
	}

	class CatchEventWithDefinitions implements EventWithDefinitions {

		private CatchEvent event;

		public CatchEventWithDefinitions(CatchEvent event) {
			this.event = event;
		}

		@Override
		public List<EventDefinition> getEventDefinitions() {
			return event.getEventDefinitions();
		}
	}

	class ThrowEventWithDefinitions implements EventWithDefinitions {

		private ThrowEvent event;

		public ThrowEventWithDefinitions(ThrowEvent event) {
			this.event = event;
		}

		@Override
		public List<EventDefinition> getEventDefinitions() {
			return event.getEventDefinitions();
		}
	}

	EventWithDefinitions create(Event event) {
		if (event instanceof CatchEvent)
			return new CatchEventWithDefinitions((CatchEvent) event);
		if (event instanceof ThrowEvent)
			return new ThrowEventWithDefinitions((ThrowEvent) event);
		return null;
	}
	
	boolean isValidTarget(Object bo) {
		return bo != null && (bo instanceof Event);
	}
	
}