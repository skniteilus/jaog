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
package com.knitelius.jaog.csv.testannotation;

import java.lang.annotation.Annotation;

import com.knitelius.jaog.csv.annotations.CSVField;

public class CSVFieldTestAnnotation implements CSVField {
	
	private String title;
	private String format;
	private String targetCSV;
	
	public CSVFieldTestAnnotation(String title, String format, String targetCSV) {
		this.title = title;
		this.format = format;
		this.targetCSV = targetCSV;
	}
	
	@Override
	public String title() {
		return title;
	}

	@Override
	public String format() {
		return format;
	}

	@Override
	public String targetCSV() {
		return targetCSV;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		// TODO Auto-generated method stub
		return null;
	}

}
