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

import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.knitelius.jaog.annotations.CSVField;
import com.knitelius.jaog.annotations.CSVOrder;
import com.knitelius.jaog.formatter.CSVFormatter;

public class CSVGenerator<T> {

	private static final char NEWLINE = '\n';

	private Class<T> beanClass;
	private Locale locale = Locale.getDefault();
	private char seperator = ';';
	private String[] order;
	private final Map<String, Field> beanFields = new HashMap<String, Field>();

	public CSVGenerator(Class<T> beanClass) {
		this.beanClass = beanClass;
		init();
	}

	public CSVGenerator(Class<T> beanClass, char seperator) {
		this.beanClass = beanClass;
		this.seperator = seperator;
		init();
	}
	
	public CSVGenerator(Class<T> beanClass, char seperator, Locale locale) {
		this.beanClass = beanClass;
		this.seperator = seperator;
		this.locale = locale;
		init();
	}

	private void init() {
		CSVOrder csvOrder = beanClass.getAnnotation(CSVOrder.class);
		if (csvOrder != null)
			order = csvOrder.value();
		Field[] fields = beanClass.getDeclaredFields();

		for (Field field : fields) {
			beanFields.put(field.getName(), field);
		}
	}

	public void writeCSVtoStream(Collection<T> beans, OutputStream out, boolean title) throws IllegalArgumentException,
			IllegalAccessException {

		PrintStream printStream = new PrintStream(out);
		
		if(title) {
			printStream = generateTitle(printStream);
		}
		
		for (T bean : beans) {
			for (String fieldName : order) {
				Field field = beanFields.get(fieldName);
				field.setAccessible(true);

				Object value = field.get(bean);
				
				value = CSVFormatter.applyFormat(value, field);
				
				printStream.print(value);
				printStream.print(seperator);
			}
			printStream.print(NEWLINE);
		}
	}

	private PrintStream generateTitle(PrintStream out) {
		for (String fieldName : order) {
			CSVField csvFieldAnnotation = beanFields.get(fieldName).getAnnotation(CSVField.class);
			if (csvFieldAnnotation != null) {
				String title = csvFieldAnnotation.title();
				if (title != null) {
					out.print(CSVFormatter.applyCSVFormat(title));
				}
				else {
					out.print(CSVFormatter.applyCSVFormat(fieldName));
				}
				out.print(seperator);
			}
		}
		out.print(NEWLINE);

		return out;
	}
}
