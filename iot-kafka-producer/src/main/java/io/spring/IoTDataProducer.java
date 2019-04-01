/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class IoTDataProducer {

	private final KafkaTemplate<String, IoTData> kafkaTemplate;

	@Value("${cloudkarafka.topic}")
	private String topic;

	IoTDataProducer(KafkaTemplate<String, IoTData> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void generateIoTEvent() throws InterruptedException {
		List<String> routeList = Arrays.asList(new String[] { "Route-37", "Route-43", "Route-82" });
		List<String> vehicleTypeList = Arrays
				.asList(new String[] { "Large Truck", "Small Truck", "Van", "18 Wheeler", "Car" });
		Random rand = new Random();
		// generate event in loop
		while (true) {
			List<IoTData> eventList = new ArrayList<IoTData>();
			for (int i = 0; i < 100; i++) {// create 100 vehicles
				String vehicleId = UUID.randomUUID().toString();
				String vehicleType = vehicleTypeList.get(rand.nextInt(5));
				String routeId = routeList.get(rand.nextInt(3));
				double speed = rand.nextInt(100 - 20) + 20;// random speed between 20 to 100
				double fuelLevel = rand.nextInt(40 - 10) + 10;
				for (int j = 0; j < 5; j++) {// Add 5 events for each vehicle
					String coords = getCoordinates(routeId);
					String latitude = coords.substring(0, coords.indexOf(","));
					String longitude = coords.substring(coords.indexOf(",") + 1, coords.length());
					// The timestamp field is set during event submission to get different values
					// across events.
					IoTData event = new IoTData(vehicleId, vehicleType, routeId, latitude, longitude, null, speed,
							fuelLevel);
					eventList.add(event);
				}
			}
			Collections.shuffle(eventList);// shuffle for random events
			for (IoTData event : eventList) {
				event.setTimestamp(new Date());
				kafkaTemplate.send("example", event);
				Thread.sleep(rand.nextInt(1000 - 500) + 500);// random delay of 0.5 to 1 second
			}
		}
	}

	// Method to generate random latitude and longitude for routes
	private String getCoordinates(String routeId) {
		Random rand = new Random();
		int latPrefix = 0;
		int longPrefix = -0;
		if (routeId.equals("Route-37")) {
			latPrefix = 33;
			longPrefix = -96;
		} else if (routeId.equals("Route-82")) {
			latPrefix = 34;
			longPrefix = -97;
		} else if (routeId.equals("Route-43")) {
			latPrefix = 35;
			longPrefix = -98;
		}
		Float lati = latPrefix + rand.nextFloat();
		Float longi = longPrefix + rand.nextFloat();
		return lati + "," + longi;
	}

}
