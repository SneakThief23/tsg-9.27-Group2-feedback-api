package com.tsg.feedbackapi.messaging;

import com.tsg.feedbackapi.dtos.FeedbackResponseDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class FeedbackEventPublisher {

    private final KafkaTemplate<String, FeedbackResponseDTO> kafkaTemplate;
    private static final String TOPIC = "feedback-submitted";

    public FeedbackEventPublisher(KafkaTemplate<String, FeedbackResponseDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishFeedbackEvent(FeedbackResponseDTO feedback) {
        kafkaTemplate.send(TOPIC, feedback.getId().toString(), feedback);
    }
}
