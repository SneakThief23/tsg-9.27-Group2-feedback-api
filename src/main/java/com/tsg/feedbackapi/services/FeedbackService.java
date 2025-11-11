package com.tsg.feedbackapi.services;

import com.tsg.feedbackapi.dtos.FeedbackRequestDTO;
import com.tsg.feedbackapi.repositories.FeedbackRepo;
import com.tsg.feedbackapi.repositories.entities.FeedbackEntity;
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

    public FeedbackEntity saveFeedback(FeedbackRequestDTO request) {
        FeedbackEntity entity = new FeedbackEntity();
        return repository.save(entity);
    }

    public FeedbackEntity getFeedbackById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
    }

    public List<FeedbackEntity> getFeedbackByMemberId(String memberId) {
        return repository.findByMemberId(memberId);
    }
}

//removed entity parts as they are mapped