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

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.knitelius.jaog.csv.annotations.CSVField;
import com.knitelius.jaog.csv.formatter.CSVFormatter;
import com.knitelius.jaog.csv.testannotation.CSVFieldTestAnnotation;

public class CSVFormatterTest {
	
	Date testDate;
	Calendar testCalendar;
	
	@SuppressWarnings("deprecation")
	@Before
	public void init() {
		testDate = new Date();
		testDate.setYear(111);
		testDate.setMonth(11);
		testDate.setDate(23);
		
		testCalendar = Calendar.getInstance();
		testCalendar.set(2011, 11, 23);
	}
	
	@Test
	public void testApplyCSVFormat() {
		assertEquals("\"word\"",CSVFormatter.applyCSVFormat("word"));
		assertEquals("\"The \"\"wild\"\" once\"", CSVFormatter.applyCSVFormat("The \"wild\" once"));
	}
	
	@Test
	public void testApplyGermanLocalizedFormattingToDate() {
		Object germanDate = CSVFormatter.applyLocalizedFormatting(testDate, Locale.GERMAN);
		assertEquals("23.12.2011", germanDate);
	}
	
	@Test
	public void testApplyUSLocalizedFormattingToDate() {
		Object usDate = CSVFormatter.applyLocalizedFormatting(testDate, Locale.US);
		assertEquals("Dec 23, 2011", usDate);
	}
	
	@Test
	public void testApplyGermanLocalizedFormattingToCalendar() {
		Object germanDate = CSVFormatter.applyLocalizedFormatting(testCalendar, Locale.GERMAN);
		assertEquals("23.12.2011", germanDate);
	}
	
	@Test
	public void testApplyUSLocalizedFormattingToCalendar() {
		Object usDate = CSVFormatter.applyLocalizedFormatting(testCalendar, Locale.US);
		assertEquals("Dec 23, 2011", usDate);
	}
	
	@Test
	public void testApplyGermanLocalizedFormattingToBigDecimal() {
		BigDecimal bigDecimal = new BigDecimal((double)1.3244).setScale(4, BigDecimal.ROUND_HALF_UP);
		Object germanDecimalFormat = CSVFormatter.applyLocalizedFormatting(bigDecimal, Locale.GERMAN);
		assertEquals("1,3244", germanDecimalFormat);
	}
	
	@Test
	public void testApplyGermanLocalizedFormattingToDouble() {
		Double doubleObj = new Double((double)1.3244);
		Object germanDouble = CSVFormatter.applyLocalizedFormatting(doubleObj, Locale.GERMAN);
		assertEquals("1,3244", germanDouble);
	}
	
	@Test
	public void testApplyGermanLocalizedFormattingToInteger() {
		Integer integerObj = new Integer(20000);
		Object germanInteger = CSVFormatter.applyLocalizedFormatting(integerObj, Locale.GERMAN);
		assertEquals("20.000", germanInteger);
	}
	
	@Test
	public void testApplyGermanLocalizedFormattingToPrimitiveLong() {
		long primitivLong = 20000;
		Object germanPrimetiveLong = CSVFormatter.applyLocalizedFormatting(primitivLong, Locale.GERMAN);
		assertEquals("20.000", germanPrimetiveLong);
	}
	@Test
	public void testApplyUSLocalizedFormattingToPrimitiveLong() {
		long primitivLong = 20000;
		Object usPrimetiveLong = CSVFormatter.applyLocalizedFormatting(primitivLong, Locale.US);
		assertEquals("20,000", usPrimetiveLong);
	}
	
	@Test
	public void testApplyGermanLocalizedFormattingToPrimitiveDouble() {
		double primitivDouble = 20000.123d;
		Object germanPrimetiveDouble = CSVFormatter.applyLocalizedFormatting(primitivDouble, Locale.GERMAN);
		assertEquals("20.000,123", germanPrimetiveDouble);
	}
	
	@Test
	public void testApplyUSLocalizedFormattingToPrimitiveDouble() {
		double primitivDouble = 20000.123d;
		Object usPrimetiveDouble = CSVFormatter.applyLocalizedFormatting(primitivDouble, Locale.US);
		assertEquals("20,000.123", usPrimetiveDouble);
	}
	
	@Test
	public void testApplyAnnotationFormattingToDate() {
		CSVField csvFieldAnnotation = new CSVFieldTestAnnotation("Date", "dd/MM/yyyy", null);
		Object formattedDate = CSVFormatter.applyFormat(testDate, csvFieldAnnotation);
		assertEquals("\"23/12/2011\"", formattedDate);
	}
	
	@Test
	public void testApplyAnnotationUSFormattingToBigDecimal() {
		CSVField csvFieldAnnotation = new CSVFieldTestAnnotation("Total", "$###,###.##", null);
		BigDecimal value = new BigDecimal((double)1200.22).setScale(2, BigDecimal.ROUND_HALF_UP);
		Object formattedBigdecimal = CSVFormatter.applyFormat(value, csvFieldAnnotation, Locale.US);
		assertEquals("\"$1,200.22\"", formattedBigdecimal);
	}
	
	@Test
	public void testApplyAnnotationGermanFormattingToBigDecimal() {
		CSVField csvFieldAnnotation = new CSVFieldTestAnnotation("Total", "\u20AC###,###.##", null);
		BigDecimal value = new BigDecimal((double)1200.22).setScale(2, BigDecimal.ROUND_HALF_UP);
		Object formattedBigdecimal = CSVFormatter.applyFormat(value, csvFieldAnnotation, Locale.GERMAN);
		assertEquals("\"Û1.200,22\"", formattedBigdecimal);
	}
	
}
