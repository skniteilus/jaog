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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.knitelius.jaog.annotations.CSVField;

public class CSVFormatter {
	
	private static final String DOUBLE_QUOTE = "\"";
	private static final String CSV_ESCAPED_DOUBLE_QUOTE = "\"\"";

	/**
	 * TODO: Description
	 * 
	 * @param value
	 * @param csvFieldAnnotation
	 * @return
	 */
	public static Object applyFormat(Object value, final CSVField csvFieldAnnotation) {
		value = applyFormat(value, csvFieldAnnotation, null);
		return value;
	}
	
	/**
	 * TODO: Description 
	 * 
	 * @param value
	 * @param csvFieldAnnotation
	 * @param locale
	 * @return
	 */
	public static Object applyFormat(Object value, final CSVField csvFieldAnnotation, Locale locale) {
		if(value==null) value = "null";
		
		if(csvFieldAnnotation!=null) {
			value = applyAnnotationFormatting(value, csvFieldAnnotation);
		}
		else if(locale!=null) {
			value = applyLocalizedFormatting(value, locale);
		}
		value = applyCSVFormat(value);
		
		return value;
	}
	
	/**
	 * Applies CSV Formatting to values, as per RFC4180
	 * 
	 * @param value
	 * @return 
	 */
	public static Object applyCSVFormat(Object value) {
		if(value == null) value = "null";
		
		value = value.toString().replace(DOUBLE_QUOTE, CSV_ESCAPED_DOUBLE_QUOTE);
		
		StringBuffer sb = new StringBuffer();
		sb.append(DOUBLE_QUOTE);
		sb.append(value);
		sb.append(DOUBLE_QUOTE);
		
		return sb.toString();
	}
	
	/**
	 * Applies Localized Formatting if no format has been specified in the annotation 
	 * 
	 * @param value
	 * @param locale
	 * @return
	 */
	private static Object applyLocalizedFormatting(Object value, Locale locale) {
		Class<?> fieldType = value.getClass();
		if(fieldType == Date.class) {
			value = DateFormat.getDateInstance(DateFormat.MEDIUM, locale).format(value);
		}
		else if (fieldType.isAssignableFrom(Number.class)) {
			value = DecimalFormat.getInstance(locale).format(value);
		}
		return value;
	}
	
	/**
	 * Applies the formatting specified in the Annotation
	 * (only for Date and Number)
	 * 
	 * @param value
	 * @param field
	 * @return
	 */
	private static Object applyAnnotationFormatting(Object value, CSVField csvFieldAnnotation) {
		String format = csvFieldAnnotation.format();
		if(format != null && format.length()>0) {
			Class<?> fieldType = value.getClass();
			
			if(fieldType == Date.class) {
				value = new SimpleDateFormat(format).format(value);
			}
			else if (fieldType.isAssignableFrom(Number.class)) {
				value = new DecimalFormat(format).format(value);
			}
		}
		return value;
	}
}
