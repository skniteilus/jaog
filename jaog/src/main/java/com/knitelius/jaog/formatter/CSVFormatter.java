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

package com.knitelius.jaog.formatter;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.knitelius.jaog.annotations.CSVField;

public class CSVFormatter {
	
	private static final char DOUBLE_QUOTE = '"';

	public static Object applyFormat(Object value, final Field field) {
		value = applyFormat(value, field, null);
		return value;
	}
	
	public static Object applyFormat(Object value, final Field field, Locale locale) {
		if(fieldHasAnnotationFormat(field)) value = applyAnnotationFormatting(value, field);
		else if(locale!=null) value = applyLocalizedFormatting(value, locale);
		value = applyCSVFormat(value);
		return value;
	}
	
	private static Object applyLocalizedFormatting(Object value, Locale locale) {
		// TODO Auto-generated method stub
		return value;
	}

	private static Object applyAnnotationFormatting(Object value, Field field) {
		CSVField csvFieldAnnotation = field.getAnnotation(CSVField.class);
		String format = csvFieldAnnotation.format();
		if(format != null && format.length()>0) {
			Class<?> fieldType = field.getType();
			
			if(fieldType == Date.class) {
				value = new SimpleDateFormat(format).format(value);
			}
			else if (fieldType.isAssignableFrom(Number.class)) {
				value = new DecimalFormat(format).format(value);
			}
		}
		return value;
	}
	
	public static Object applyCSVFormat(Object value) {
		
		value = value.toString().replace("\"", "\"\"");
		
		StringBuffer sb = new StringBuffer();
		sb.append(DOUBLE_QUOTE);
		sb.append(value);
		sb.append(DOUBLE_QUOTE);
		
		return sb.toString();
	}
	
	private static boolean fieldHasAnnotationFormat(Field field) {
		CSVField csvFieldAnnotation = field.getAnnotation(CSVField.class);
		String format = csvFieldAnnotation.format();
		return (format != null && format.length()>0);
	}
}
