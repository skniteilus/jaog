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
package com.knitelius.jaog.csv.processor;

import static org.junit.Assert.*;

import java.beans.IntrospectionException;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.knitelius.jaog.csv.generator.CSVGenerator;
import com.knitelius.jaog.csv.generator.CSVGeneratorFactory;
import com.knitelius.jaog.csv.testobjects.Audi;
import com.knitelius.jaog.csv.testobjects.BMW;
import com.knitelius.jaog.csv.testobjects.Car;
import com.knitelius.jaog.csv.testobjects.Person;

public class CSVGeneratorTest {

	private Collection<Person> orderedBeanTestData = new ArrayList<Person>();
	private Collection<Car> orderedInterfacedBeanTestData = new ArrayList<Car>();
	
	@Before
	public void init() {
		orderedBeanTestData.add(new Person("James", "Bond", new Date()));
		orderedBeanTestData.add(new Person("Clive", "Cussler", new Date()));
		orderedBeanTestData.add(new Person("Dirk", "Pitt", new Date()));

		orderedInterfacedBeanTestData.add(new Audi("A3", "K-XX-200", new Date()));
		orderedInterfacedBeanTestData.add(new Audi("A4", "KO-XX-300", new Date()));
		orderedInterfacedBeanTestData.add(new Audi("A5", "K-RR-400", new Date()));
		orderedInterfacedBeanTestData.add(new Audi("A9", null, null));
		orderedInterfacedBeanTestData.add(new BMW("7", null, new Date()));
		orderedInterfacedBeanTestData.add(new BMW("3", "KO-HN-323", new Date()));
	}
	
	//TODO: Assert statement is missing
	@Test
	public void testOrderedCSV() throws IllegalArgumentException, IllegalAccessException, IntrospectionException,
			InvocationTargetException, SecurityException, NoSuchFieldException, IOException {
		CSVGenerator<Person> csvGenerator = CSVGeneratorFactory.getCSVGenerator(Person.class);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		csvGenerator.writeCSVtoStream(orderedBeanTestData, baos, true);
		String csv = baos.toString("UTF8");
		System.out.println(csv);
	}
	
	//TODO: Assert statement is missing
	@Test
	public void testInterfaceBeans() throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, SecurityException, IntrospectionException, NoSuchFieldException, IOException {
		CSVGenerator<Car> csvGenerator = CSVGeneratorFactory.getCSVGenerator(Car.class);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		csvGenerator.writeCSVtoStream(orderedInterfacedBeanTestData, baos, true);
		String csv = baos.toString("UTF8");
		System.out.println(csv);
	}

}
