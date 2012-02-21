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
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Locale;

import com.knitelius.jaog.annotations.CSVField;
import com.knitelius.jaog.annotations.CSVOrder;
import com.knitelius.jaog.formatter.CSVFormatter;

public class InterfacedBeanCSVGenerator<T> extends BaseCSVGenerator<T> {

	public InterfacedBeanCSVGenerator(Class<T> beanClass) throws IntrospectionException, SecurityException,
			NoSuchFieldException {
		super(beanClass);
	}

	public InterfacedBeanCSVGenerator(Class<T> beanClass, char seperator) throws IntrospectionException,
			SecurityException, NoSuchFieldException {
		super(beanClass, seperator);
	}

	public InterfacedBeanCSVGenerator(Class<T> beanClass, char seperator, Locale locale) throws IntrospectionException,
			SecurityException, NoSuchFieldException {
		super(beanClass, seperator, locale);
	}

	public InterfacedBeanCSVGenerator(Class<T> beanClass, char delimiter, Locale locale, String encoding)
			throws IntrospectionException, SecurityException, NoSuchFieldException {
		super(beanClass, delimiter, locale, encoding);
	}

	protected void init() throws SecurityException, IntrospectionException, NoSuchFieldException {
		super.init();
		CSVOrder csvOrder = beanClass.getAnnotation(CSVOrder.class);
		if (csvOrder != null) {
			order = csvOrder.value();
		}
	}

	@Override
	protected void printLines(Collection<T> beans, Writer writer)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {

		for (T bean : beans) {
			for (String fieldName : order) {
				CSVField csvFieldAnnotation = getCSVFieldAnnotation(fieldName);
				Method method = getterMethods.get(fieldName);
				Object value = method.invoke(bean);
				writer.write(CSVFormatter.applyFormat(value, csvFieldAnnotation, locale));
				writer.write(delimiter);
			}
			writer.write(NEWLINE);
		}
	}
}
