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
package com.knitelius.jaog.csv.testobjects;

import java.util.Date;

public class Audi implements Car {
	
	private String model;
	private String licensePlate;
	private Date registrationDate;
	
	public Audi(String model, String licensePlate, Date registrationDate) {
		this.model = model;
		this.licensePlate = licensePlate;
		this.registrationDate = registrationDate;
	}
		
	@Override
	public String getModel() {
		return model;
	}

	@Override
	public String getLicensePlate() {
		return licensePlate;
	}
	
	@Override
	public Date getRegistrationDate() {
		return registrationDate;
	}

}
