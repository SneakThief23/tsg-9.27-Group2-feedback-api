package com.tsg.feedbackapi.services;

import com.tsg.feedbackapi.dtos.FeedbackRequestDTO;
import com.tsg.feedbackapi.dtos.FeedbackResponseDTO;
import com.tsg.feedbackapi.mappers.FeedbackMapper;
import com.tsg.feedbackapi.repositories.FeedbackRepo;
import com.tsg.feedbackapi.repositories.entities.FeedbackEntity;
import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;
import java.time.OffsetDateTime;

@Service
public class FeedbackService {

    private final FeedbackRepo repository;
    private final KafkaTemplate<String, FeedbackResponseDTO> kafkaTemplate;
    private final FeedbackMapper mapper;


    public FeedbackService(FeedbackRepo repository,  KafkaTemplate<String, FeedbackResponseDTO> kafkaTemplate, FeedbackMapper mapper) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }
    @Transactional
    public FeedbackEntity saveFeedback(FeedbackRequestDTO request) {
        FeedbackEntity entity = new FeedbackEntity();

        entity.setMemberId(request.getMemberId());
        entity.setProviderName(request.getProviderName());
        entity.setRating(request.getRating());
        entity.setComment(request.getComment());
        entity.setSubmittedAt(OffsetDateTime.now());

        FeedbackEntity saved = repository.save(entity);

        kafkaTemplate.send("feedback-submitted", saved.getId().toString(), mapper.toResponse(saved));

        return  saved;
    }

    public FeedbackEntity getFeedbackById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Feedback not found: " + id));
    }

    public List<FeedbackEntity> getFeedbackByMemberId(String memberId) {
        return repository.findByMemberId(memberId);
    }
}