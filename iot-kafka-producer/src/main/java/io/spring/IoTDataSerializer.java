package io.spring;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IoTDataSerializer implements Serializer<IoTData> {

	private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // nothing to do
    }

    @Override
    public byte[] serialize(String topic, IoTData iotEvent) {
    	try {
			String msg = objectMapper.writeValueAsString(iotEvent);
			return msg.getBytes();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
    }

    @Override
    public void close() {
        // nothing to do
    }
    
}