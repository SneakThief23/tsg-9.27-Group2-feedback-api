package com.tsg.feedbackapi.services;

import com.tsg.feedbackapi.dtos.FeedbackRequestDTO;
import com.tsg.feedbackapi.repositories.FeedbackRepo;
import com.tsg.feedbackapi.repositories.entities.Feedback;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;
import java.time.OffsetDateTime;

@Service
public class FeedbackService {

    private final FeedbackRepo repository;

    public FeedbackService(FeedbackRepo repository) {
        this.repository = repository;
    }

    public Feedback saveFeedback(FeedbackRequestDTO request) {
        Feedback entity = new Feedback();
        entity.setMemberId(request.getMemberId());
        entity.setProviderName(request.getProviderName());
        entity.setRating(request.getRating());
        entity.setComment(request.getComment());
        entity.setSubmittedAt(OffsetDateTime.now());

        return repository.save(entity);
    }

    public Feedback getFeedbackById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
    }

    public List<Feedback> getFeedbackByMemberId(String memberId) {
        return repository.findByMemberId(memberId);
    }
}
