/*
 * Copyright 2012 Stephan Knitelius
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.knitelius.jaog.generator;

import java.beans.IntrospectionException;
import java.util.Locale;

import com.knitelius.jaog.annotations.CSVOrder;

public class CSVGeneratorFactory {
	
	private static final char DEFAULT_DELIMITER = ';';
	
	/**
	 * TODO: Description
	 * 
	 * @param beanClass
	 * @return
	 * @throws IntrospectionException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static <T> CSVGenerator<T> getCSVGenerator(Class<T> beanClass) throws IntrospectionException, SecurityException, NoSuchFieldException {
		return getCSVGenerator(beanClass, null, DEFAULT_DELIMITER);
	}
	
	/**
	 * TODO: Description
	 * 
	 * @param beanClass
	 * @param locale
	 * @return
	 * @throws IntrospectionException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static <T> CSVGenerator<T> getCSVGenerator(Class<T> beanClass, Locale locale) throws IntrospectionException, SecurityException, NoSuchFieldException {
		return getCSVGenerator(beanClass, locale, DEFAULT_DELIMITER);
	}
	
	/**
	 * TODO: Description
	 * 
	 * @param beanClass
	 * @return
	 * @throws IntrospectionException 
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	public static <T> CSVGenerator<T> getCSVGenerator(Class<T> beanClass, Locale locale, char delimiter) throws IntrospectionException, SecurityException, NoSuchFieldException {
		if(beanClass==null) throw new IllegalArgumentException("Class may not be null!");
		
		if(beanClass.isInterface()) {
			return new InterfacedBeanCSVGenerator<T>(beanClass, delimiter, locale);
		}
		else if(beanClass.getAnnotation(CSVOrder.class) != null) {
			return new OrderedCSVGenerator<T>(beanClass, delimiter, locale);
		}
		else {
			return new DefaultCSVGenerator<T>(beanClass, delimiter, locale);
		}
	}
}
