package com.instantiations.designer.databinding;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Realm;

/**
 * This class may be freely distributed as part of any application or plugin.
 * <p>
 * Copyright (c) 2008, Instantiations, Inc. <br>All Rights Reserved
 * 
 * @author lobas_av
 */
public class BeansSetObservableFactory extends BeansObservableFactory {
	private final String m_propertyName;
	////////////////////////////////////////////////////////////////////////////
	//
	// Constructor
	//
	////////////////////////////////////////////////////////////////////////////
	public BeansSetObservableFactory(Class<?> beanClass, String propertyName) {
		super(beanClass);
		m_propertyName = propertyName;
	}
	////////////////////////////////////////////////////////////////////////////
	//
	// BeansObservableFactory
	//
	////////////////////////////////////////////////////////////////////////////
	@Override
	protected IObservable createBeanObservable(Object target) {
		return BeansObservables.observeSet(Realm.getDefault(), target, m_propertyName);
	}
}