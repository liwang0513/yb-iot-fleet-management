package io.spring;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IoTDataDeserializer implements Deserializer<IoTData> {
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// nothing to do
	}

	@Override
	public IoTData deserialize(String topic, byte[] data) {
		try {
			IoTData event = objectMapper.readValue(new String(data), IoTData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void close() {
		// nothing to do
	}

}
