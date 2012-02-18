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

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.knitelius.jaog.generator.CSVGenerator;
import com.knitelius.jaog.generator.CSVGeneratorFactory;
import com.knitelius.jaog.testobjects.Audi;
import com.knitelius.jaog.testobjects.BMW;
import com.knitelius.jaog.testobjects.Car;
import com.knitelius.jaog.testobjects.Person;

public class CSVGeneratorTest {

	private Collection<Person> orderedBeanTestData = new ArrayList<Person>();
	private Collection<Car> orderedInterfacedBeanTestData = new ArrayList<Car>();

	public CSVGeneratorTest() {
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

	@Test
	public void test() throws IllegalArgumentException, IllegalAccessException, IntrospectionException,
			InvocationTargetException, SecurityException, NoSuchFieldException {
		CSVGenerator<Person> csvGenerator = CSVGeneratorFactory.getCSVGenerator(Person.class);
		csvGenerator.writeCSVtoStream(orderedBeanTestData, System.out, true);
	}

	@Test
	public void testInterfaceBeans() throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, SecurityException, IntrospectionException, NoSuchFieldException {
		CSVGenerator<Car> csvGenerator = CSVGeneratorFactory.getCSVGenerator(Car.class);
		csvGenerator.writeCSVtoStream(orderedInterfacedBeanTestData, System.out, true);
	}

}
