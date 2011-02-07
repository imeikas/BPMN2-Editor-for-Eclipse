package com.instantiations.designer.databinding;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.eclipse.core.databinding.BindingException;

/**
 * This class may be freely distributed as part of any application or plugin.
 * <p>
 * Copyright (c) 2008, Instantiations, Inc. <br>All Rights Reserved
 * 
 * @author lobas_av
 */
/*package*/final class Utils {
	private static final Object[] EMPTY_ARRAY = new Object[0];
	////////////////////////////////////////////////////////////////////////////
	//
	// Utils
	//
	////////////////////////////////////////////////////////////////////////////
	public static Method getMethod(Class<?> beanClass, String propertyName) {
		if (propertyName != null) {
			PropertyDescriptor descriptor = getPropertyDescriptor(beanClass, propertyName);
			if (descriptor != null) {
				return descriptor.getReadMethod();
			}
		}
		return null;
	}
	public static Object invokeMethod(Method method, Class<?> beanClass, Object element) {
		if (method != null && instanceOf(beanClass, element)) {
			try {
				return method.invoke(element, EMPTY_ARRAY);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}
	public static boolean instanceOf(Class<?> beanClass, Object element) {
		return element != null && beanClass.isAssignableFrom(element.getClass());
	}
	private static PropertyDescriptor getPropertyDescriptor(Class<?> beanClass, String propertyName) {
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(beanClass);
		} catch (IntrospectionException e) {
			return null;
		}
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			if (descriptor.getName().equals(propertyName)) {
				return descriptor;
			}
		}
		throw new BindingException("Could not find property with name "
			+ propertyName
			+ " in class "
			+ beanClass);
	}
}