package com.instantiations.designer.databinding;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;

/**
 * This class may be freely distributed as part of any application or plugin.
 * <p>
 * Copyright (c) 2008, Instantiations, Inc. <br>All Rights Reserved
 * 
 * @author lobas_av
 */
/*package*/abstract class BeansObservableFactory implements IObservableFactory {
	private final Class<?> m_beanClass;
	////////////////////////////////////////////////////////////////////////////
	//
	// Constructor
	//
	////////////////////////////////////////////////////////////////////////////
	public BeansObservableFactory(Class<?> beanClass) {
		m_beanClass = beanClass;
	}
	////////////////////////////////////////////////////////////////////////////
	//
	// IObservableFactory
	//
	////////////////////////////////////////////////////////////////////////////
	@Override
	public IObservable createObservable(Object target) {
		if (target instanceof IObservable) {
			return (IObservable) target;
		}
		if (Utils.instanceOf(m_beanClass, target)) {
			return createBeanObservable(target);
		}
		return null;
	}
	/**
	 * Creates an observable for the given target object.
	 */
	protected abstract IObservable createBeanObservable(Object target);
}