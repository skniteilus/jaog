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
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import com.knitelius.jaog.formatter.CSVFormatter;

public class DefaultCSVGenerator<T> extends BaseCSVGenerator<T> {

	private Collection<PropertyDescriptor> getterMethodPD = new ArrayList<PropertyDescriptor>();

	public DefaultCSVGenerator(Class<T> beanClass) throws IntrospectionException, SecurityException, NoSuchFieldException {
		super(beanClass);
	}

	public DefaultCSVGenerator(Class<T> beanClass, char delimiter) throws IntrospectionException, SecurityException, NoSuchFieldException {
		super(beanClass, delimiter);
	}

	public DefaultCSVGenerator(Class<T> beanClass, char delimiter, Locale locale) throws IntrospectionException, SecurityException, NoSuchFieldException {
		super(beanClass, delimiter, locale);
	}
	
	public DefaultCSVGenerator(Class<T> beanClass, char delimiter, Locale locale, String encoding)
			throws IntrospectionException, SecurityException, NoSuchFieldException {
		super(beanClass, delimiter, locale, encoding);
	}

	protected void init() throws IntrospectionException, SecurityException, NoSuchFieldException {
		for (PropertyDescriptor pd : Introspector.getBeanInfo(beanClass).getPropertyDescriptors()) {
			if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
				getterMethodPD.add(pd);
				Field field = beanClass.getField(pd.getName());
				beanFields.put(field.getName(), field);
			}
		}
	}

	@Override
	protected Writer generateTitle(Writer writer) throws IOException {
		for(PropertyDescriptor gMpD : getterMethodPD) {
			writer.write(CSVFormatter.applyCSVFormat(gMpD.getName()));
			writer.write(delimiter);
		}
		writer.write(NEWLINE);
		return writer;
	}

	@Override
	protected void printLines(Collection<T> beans, Writer writer) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		for(T bean : beans) {
			for(PropertyDescriptor gMpD : getterMethodPD) {
				writer.write(CSVFormatter.applyCSVFormat(gMpD.getReadMethod().invoke(bean)));
				writer.write(delimiter);
			}
			writer.write(NEWLINE);
		}
	}

}
