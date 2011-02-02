package org.jboss.bpmn2.editor.core;

import java.beans.PropertyChangeListener;

import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeature;

public class ToolEnablement {
	
	
	@Override
    public String toString() {
	    return "ToolEnablement [feature=" + feature + ", enabled=" + getEnabled() + "]";
    }

	IFeature feature;
	private Boolean enabled;

	public void setFeature(IFeature feature) {
		this.feature = feature;
	}

	public IFeature getFeature() {
		return feature;
	}

	public void setEnabled(Boolean enabled) {
	    this.enabled = enabled;
    }

	public String getName(){
		if(feature instanceof ICreateConnectionFeature){
			return ((ICreateConnectionFeature) feature).getCreateName();
		}
		return feature.getName();
	}
	
	public String getCanonicalName(){
		return feature.getClass().getCanonicalName();
	}
	
	public Boolean getEnabled() {
	    return enabled;
    }
	
	public void removePropertyChangeListener(PropertyChangeListener pcl){
	}

	public void removePropertyChangeListener(String s, PropertyChangeListener pcl){
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl){
	}
	
	public void addPropertyChangeListener(String s,PropertyChangeListener pcl){
	}
	
}