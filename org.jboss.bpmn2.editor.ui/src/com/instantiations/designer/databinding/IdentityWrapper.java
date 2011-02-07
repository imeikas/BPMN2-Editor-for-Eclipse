package com.instantiations.designer.databinding;

/**
 * Used for wrapping objects that define their own implementations of <code>equals()</code> and
 * <code>hashCode()</code> when putting them in sets or hashmaps to ensure identity comparison.
 * 
 * This class may be freely distributed as part of any application or plugin.
 * <p>
 * Copyright (c) 2008, Instantiations, Inc. <br>All Rights Reserved
 * 
 * @author lobas_av
 */
/*package*/final class IdentityWrapper {
	private final Object m_object;
	////////////////////////////////////////////////////////////////////////////
	//
	// Constructor
	//
	////////////////////////////////////////////////////////////////////////////
	public IdentityWrapper(Object object) {
		m_object = object;
	}
	////////////////////////////////////////////////////////////////////////////
	//
	// Access
	//
	////////////////////////////////////////////////////////////////////////////
	public Object unwrap() {
		return m_object;
	}
	////////////////////////////////////////////////////////////////////////////
	//
	// Object
	//
	////////////////////////////////////////////////////////////////////////////
	@Override
	public int hashCode() {
		return System.identityHashCode(m_object);
	}
	@Override
	public boolean equals(Object object) {
		if (object == null || object.getClass() != IdentityWrapper.class) {
			return false;
		}
		IdentityWrapper wrapper = (IdentityWrapper) object;
		return m_object == wrapper.m_object;
	}
}