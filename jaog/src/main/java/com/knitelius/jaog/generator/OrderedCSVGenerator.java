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
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Locale;

import com.knitelius.jaog.annotations.CSVOrder;
import com.knitelius.jaog.formatter.CSVFormatter;

public class OrderedCSVGenerator<T> extends BaseCSVGenerator<T> {

	public OrderedCSVGenerator(Class<T> beanClass) throws IntrospectionException, SecurityException,
			NoSuchFieldException {
		super(beanClass);
	}

	public OrderedCSVGenerator(Class<T> beanClass, char delimiter) throws IntrospectionException, SecurityException,
			NoSuchFieldException {
		super(beanClass, delimiter);
	}

	public OrderedCSVGenerator(Class<T> beanClass, char delimiter, Locale locale) throws IntrospectionException,
			SecurityException, NoSuchFieldException {
		super(beanClass, delimiter, locale);
	}

	public OrderedCSVGenerator(Class<T> beanClass, char delimiter, Locale locale, String encoding) throws IntrospectionException,
			SecurityException, NoSuchFieldException {
		super(beanClass, delimiter, locale, encoding);
	}

	protected void init() throws SecurityException, IntrospectionException, NoSuchFieldException {
		super.init();
		CSVOrder csvOrder = beanClass.getAnnotation(CSVOrder.class);
		if (csvOrder != null) {
			order = csvOrder.value();
		}
	}

	protected void printLines(Collection<T> beans, Writer writer)
			throws IllegalArgumentException, IllegalAccessException, IOException {
		for (T bean : beans) {
			for (String fieldName : order) {
				Field field = beanFields.get(fieldName);
				field.setAccessible(true);

				Object value = field.get(bean);

				String formattedValue = CSVFormatter.applyFormat(value, getCSVFieldAnnotation(fieldName), locale);

				writer.write(formattedValue);
				writer.write(delimiter);
			}
			writer.write(NEWLINE);
		}
	}
}
