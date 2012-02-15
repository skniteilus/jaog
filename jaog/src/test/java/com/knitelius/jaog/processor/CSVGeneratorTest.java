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
package com.knitelius.jaog.processor;

import static org.junit.Assert.*;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.knitelius.jaog.generator.CSVGenerator;
import com.knitelius.jaog.testobjects.Person;

public class CSVGeneratorTest {
	
	Collection<Person> testData = new ArrayList<Person>();
	
	public CSVGeneratorTest() {
		testData.add(new Person("James","Bond",new Date()));
		testData.add(new Person("Clive","Cussler",new Date()));
		testData.add(new Person("Dirk","Pitt",new Date()));
	}
	
	@Test
	public void test() throws IllegalArgumentException, IllegalAccessException {
		CSVGenerator<Person> csvGenerator = new CSVGenerator<Person>(Person.class);
		csvGenerator.writeCSVtoStream(testData, System.out, true);
	}

}
