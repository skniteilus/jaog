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

package com.knitelius.jaog.csv.formatter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.knitelius.jaog.csv.annotations.CSVField;

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
	public static String applyFormat(Object value, final CSVField csvFieldAnnotation) {
		value = applyFormat(value, csvFieldAnnotation, Locale.getDefault());
		return value.toString();
	}
	
	/**
	 * Formats input either by given Annotation or Locale
	 * If no format is provided for Date or Calendar objects DateFormat.MEDIUM is applied.
	 * 
	 * @param value
	 * @param csvFieldAnnotation
	 * @param locale (if null JVM Default locale is applied)
	 * @return 
	 */
	public static String applyFormat(Object value, final CSVField csvFieldAnnotation, Locale locale) {
		if(value==null) value = "null";
		if(locale==null) locale = Locale.getDefault();
		
		if(csvFieldAnnotation != null) {
			value = applyAnnotationFormatting(value, csvFieldAnnotation, locale);
		}
		else if(locale != null) {
			value = applyLocalizedFormatting(value, locale);
		}
		value = applyCSVFormat(value);
		
		return value.toString();
	}
	
	/**
	 * Applies CSV Formatting to values, as per RFC4180
	 * 
	 * @param value
	 * @return 
	 */
	public static String applyCSVFormat(Object value) {
		if(value == null) value = "null";
		
		value = value.toString().replace(DOUBLE_QUOTE, CSV_ESCAPED_DOUBLE_QUOTE);
		
		StringBuffer sb = new StringBuffer();
		sb.append(DOUBLE_QUOTE);
		sb.append(value);
		sb.append(DOUBLE_QUOTE);
		
		return sb.toString();
	}
	
	/**
	 * Applies Localized Formatting if no format has been specified in the annotation.
	 * DateFormat.MEDIUM is applied by default to Date or Calendar Objects 
	 * 
	 * @param value
	 * @param locale
	 * @return 
	 */
	protected static String applyLocalizedFormatting(Object value, Locale locale) {
		if(value == null) throw new IllegalArgumentException("Value may not be null");
		if(locale == null) throw new IllegalArgumentException("Locale may not be null");
		
		Class<?> fieldType = value.getClass();
		if(Date.class == fieldType) {
			value = DateFormat.getDateInstance(DateFormat.MEDIUM, locale).format(value);
		}
		else if(Calendar.class.isAssignableFrom(fieldType)) {
			value = DateFormat.getDateInstance(DateFormat.MEDIUM, locale).format(((Calendar)value).getTime());
		}
		else if (Number.class.isAssignableFrom(fieldType)) {
			//FIXME: output decimal places as per input - currently 3 DP are returned
			value = DecimalFormat.getInstance(locale).format(value);
		}
		return value.toString();
	}
	
	/**
	 * Applies the formatting specified in the Annotation
	 * (only for Date and Number)
	 * 
	 * @param value
	 * @param field
	 * @return
	 */
	protected static String applyAnnotationFormatting(Object value, CSVField csvFieldAnnotation, Locale locale) {
		if(value == null) throw new IllegalArgumentException("Value may not be null");
		if(csvFieldAnnotation == null) throw new IllegalArgumentException("CSVField may not be null");
		
		String format = csvFieldAnnotation.format();
		if(format != null && format.length()>0) {
			Class<?> fieldType = value.getClass();
			
			if(fieldType == Date.class) {
				value = new SimpleDateFormat(format).format(value);
			}
			else if(Calendar.class.isAssignableFrom(fieldType)) {
				value = new SimpleDateFormat(format).format(((Calendar)value).getTime());
			}
			else if (Number.class.isAssignableFrom(fieldType)) {
				DecimalFormat df = (DecimalFormat)DecimalFormat.getInstance(locale);
				df.applyPattern(format);
				value = df.format(value);
			}
		}
		return value.toString();
	}
}
