package com.tsg.feedbackapi.services;

import com.tsg.feedbackapi.dtos.FeedbackRequestDTO;
import com.tsg.feedbackapi.dtos.FeedbackResponseDTO;
import com.tsg.feedbackapi.mappers.FeedbackMapper;
import com.tsg.feedbackapi.repositories.FeedbackRepo;
import com.tsg.feedbackapi.repositories.entities.FeedbackEntity;
import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import java.time.OffsetDateTime;

@Service
public class FeedbackService {

    private final FeedbackRepo repository;
    private final KafkaTemplate<String, FeedbackResponseDTO> kafkaTemplate;
    private final FeedbackMapper mapper;


    public FeedbackService(FeedbackRepo repository, KafkaTemplate<String, FeedbackResponseDTO> kafkaTemplate, FeedbackMapper mapper) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }

    @Transactional
    public FeedbackResponseDTO saveFeedback(FeedbackRequestDTO request) {

        validate(request);

        FeedbackEntity entity = new FeedbackEntity();

        entity.setMemberId(request.getMemberId());
        entity.setProviderName(request.getProviderName());
        entity.setRating(request.getRating());
        entity.setComment(request.getComment());
        entity.setSubmittedAt(OffsetDateTime.now());

        FeedbackEntity saved = repository.save(entity);

        kafkaTemplate.send("feedback-submitted", saved.getId().toString(), mapper.toResponse(saved));

        return mapper.toResponse(saved);
    }

    public FeedbackResponseDTO getFeedbackById(UUID id) {
        FeedbackEntity feedbackEntity = repository.findById(id).orElseThrow(() -> new RuntimeException("Feedback not found: " + id));
        return mapper.toResponse(feedbackEntity);
    }

    public List<FeedbackResponseDTO> getFeedbackByMemberId(String memberId) {
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("Member id must not be empty");
        }
        return repository.findByMemberId(memberId).stream()
                .map(en -> mapper.toResponse(en)).toList();
    }


    private void validate(FeedbackRequestDTO req) {

        System.out.println("Validating request: " + req);

        List<ValidationException.ValidationError> errors = new ArrayList<>();

        if (req.getMemberId() == null || req.getMemberId().isBlank() || req.getMemberId().isEmpty()) {
            errors.add(new ValidationException.ValidationError("memberId", "Member ID is required"));
        } else if (req.getMemberId().length() > 36) {
            errors.add(new ValidationException.ValidationError("memberId", "Must be ≤ 36 characters"));
        }

        if (req.getProviderName() == null || req.getProviderName().isBlank()) {
            errors.add(new ValidationException.ValidationError("providerName", "Provider Name is required"));
        } else if (req.getProviderName().length() > 80) {
            errors.add(new ValidationException.ValidationError("providerName", "Provider Name must be ≤ 80 characters"));
        }

        if (req.getRating() == null) {
            errors.add(new ValidationException.ValidationError("rating", "Rating is required"));
        } else if (req.getRating() < 1 || req.getRating() > 5) {
            errors.add(new ValidationException.ValidationError("rating", "Rating must be between 1 and 5"));
        }

        if (req.getComment() != null && req.getComment().length() > 200) {
            errors.add(new ValidationException.ValidationError("comment", "Comment must be ≤ 200 characters"));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

}