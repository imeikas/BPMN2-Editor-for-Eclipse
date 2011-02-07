package com.instantiations.designer.databinding;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * This class may be freely distributed as part of any application or plugin.
 * <p>
 * Copyright (c) 2008, Instantiations, Inc. <br>
 * All Rights Reserved
 * 
 * @author lobas_av
 */
public class EMFBeansListObservableFactory extends BeansObservableFactory {
	private final EStructuralFeature m_eStructuralFeature;
	////////////////////////////////////////////////////////////////////////////
	//
	// Constructor
	//
	////////////////////////////////////////////////////////////////////////////
	public EMFBeansListObservableFactory(Class<?> beanClass, EStructuralFeature eStructuralFeature) {
		super(beanClass);
		m_eStructuralFeature = eStructuralFeature;
	}
	////////////////////////////////////////////////////////////////////////////
	//
	// BeansObservableFactory
	//
	////////////////////////////////////////////////////////////////////////////
	@Override
	protected IObservable createBeanObservable(Object target) {
		if (target instanceof EObject) {
			return EMFObservables.observeList(Realm.getDefault(), (EObject) target, m_eStructuralFeature);
		}
		return null;
	}
}