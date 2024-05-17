package org.pucar.dristi.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Consumer {

	/*
	 * Uncomment the below line to start consuming messages from kafka.topics.consumer
	 * The value of the variable kafka.topics.consumer should be overwritten in
	 * application.properties
	 */
	@KafkaListener(topics = "${kafka.topics.consumer}")
	public void listen(final Map<String, Object> message) {
		// Process the incoming message
		processMessage(message);
	}

	/**
	 * Processes the incoming Kafka message.
	 *
	 * @param message the Kafka message to process
	 */
	private void processMessage(Map<String, Object> message) {
		//TODO
	}
}
