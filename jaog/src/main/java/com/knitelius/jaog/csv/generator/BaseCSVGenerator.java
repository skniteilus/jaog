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
package com.knitelius.jaog.csv.generator;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.knitelius.jaog.csv.annotations.CSVField;
import com.knitelius.jaog.csv.formatter.CSVFormatter;

public abstract class BaseCSVGenerator<T> implements CSVGenerator<T> {

	protected static final char NEWLINE = '\n';
	protected static final String DEFAULT_ENCODING = "UTF8";

	protected Class<T> beanClass;
	protected Locale locale = Locale.getDefault();
	protected char delimiter = ';';
	protected String encoding;
	protected String[] order;
	protected Map<String, Field> beanFields = new HashMap<String, Field>();
	protected Map<String, Method> getterMethods = new HashMap<String, Method>();
	protected Collection<PropertyDescriptor> getterMethodPD = new ArrayList<PropertyDescriptor>();

	public BaseCSVGenerator(Class<T> beanClass) throws IntrospectionException, SecurityException, NoSuchFieldException {
		this.beanClass = beanClass;
		this.encoding = DEFAULT_ENCODING;
		init();
	}

	public BaseCSVGenerator(Class<T> beanClass, char delimiter) throws IntrospectionException, SecurityException,
			NoSuchFieldException {
		this.beanClass = beanClass;
		this.delimiter = delimiter;
		this.encoding = DEFAULT_ENCODING;
		init();
	}

	public BaseCSVGenerator(Class<T> beanClass, char delimiter, Locale locale) throws IntrospectionException,
			SecurityException, NoSuchFieldException {
		this.beanClass = beanClass;
		this.delimiter = delimiter;
		this.locale = locale;
		this.encoding = DEFAULT_ENCODING;
		init();
	}

	public BaseCSVGenerator(Class<T> beanClass, char delimiter, Locale locale, String encoding) throws IntrospectionException,
			SecurityException, NoSuchFieldException {
		this.beanClass = beanClass;
		this.delimiter = delimiter;
		this.locale = locale;
		this.encoding = encoding;
		init();
	}

	protected void init() throws IntrospectionException, SecurityException, NoSuchFieldException {
		for (PropertyDescriptor pd : Introspector.getBeanInfo(beanClass).getPropertyDescriptors()) {
			if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
				getterMethodPD.add(pd);

				getterMethods.put(pd.getName(), pd.getReadMethod());
				if (!beanClass.isInterface()) {
					Field field = beanClass.getDeclaredField(pd.getName());
					beanFields.put(field.getName(), field);
				}
			}
		}
		order = getterMethods.keySet().toArray(new String[0]);
	}

	public void writeCSVtoStream(Collection<T> beans, OutputStream out, boolean title) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, IOException {

		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out, encoding);
		Writer writer = new BufferedWriter(outputStreamWriter);

		if (title) {
			writer = generateTitle(writer);
		}
		printLines(beans, writer);
		writer.flush();
		writer.close();
	}

	protected abstract void printLines(Collection<T> beans, Writer writer)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException;

	protected Writer generateTitle(Writer writer) throws IOException {
		for (String fieldName : order) {
			CSVField csvFieldAnnotation = getCSVFieldAnnotation(fieldName);

			String title = csvFieldAnnotation != null && csvFieldAnnotation.title().length() > 0 ? csvFieldAnnotation
					.title() : fieldName;

			writer.write(CSVFormatter.applyCSVFormat(title));
			writer.write(delimiter);
		}
		writer.write(NEWLINE);

		return writer;
	}

	/**
	 * Returns the CSVField Annotation of either the method or the field Null is
	 * returned if no annotation is found
	 * 
	 * @param fieldName
	 * @return CSVField Annotation
	 */
	protected CSVField getCSVFieldAnnotation(String fieldName) {

		Method getterMethod = getterMethods.get(fieldName);

		if (getterMethod != null) {
			CSVField csvField = getterMethod.getAnnotation(CSVField.class);
			if (csvField != null)
				return csvField;
		}

		Field field = beanFields.get(fieldName);
		if (field != null) {
			CSVField csvFieldAnnotation = field.getAnnotation(CSVField.class);
			return csvFieldAnnotation;
		}
		return null;
	}
}
