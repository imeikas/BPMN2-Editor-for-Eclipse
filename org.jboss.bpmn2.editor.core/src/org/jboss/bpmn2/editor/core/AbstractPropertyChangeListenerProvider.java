package org.jboss.bpmn2.editor.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public abstract class AbstractPropertyChangeListenerProvider {

	ArrayList<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		listeners.remove(pcl);
	}

	public void removePropertyChangeListener(String s, PropertyChangeListener pcl) {
		listeners.remove(pcl);
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		listeners.add(pcl);
	}

	public void addPropertyChangeListener(String s, PropertyChangeListener pcl) {
		listeners.add(pcl);
	}

	protected void fireChangeEvent(PropertyChangeEvent propertyChangeEvent) {
		for (PropertyChangeListener l : listeners) {
			l.propertyChange(propertyChangeEvent);
		}
	}

}