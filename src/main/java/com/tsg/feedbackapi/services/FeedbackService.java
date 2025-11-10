package com.tsg.feedbackapi.services;

import com.tsg.feedbackapi.dtos.FeedbackRequest;
import com.tsg.feedbackapi.repositories.FeedbackRepository;
import com.tsg.feedbackapi.repositories.entities.FeedbackEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class FeedbackService {

    private final FeedbackRepository repository;

    public FeedbackService(FeedbackRepository repository) {
        this.repository = repository;
    }

    public FeedbackEntity saveFeedback(FeedbackRequest request) {
        FeedbackEntity entity = new FeedbackEntity();
        entity.setMemberId(request.getMemberId());
        entity.setProviderName(request.getProviderName());
        entity.setRating(request.getRating());
        entity.setComment(request.getComment());
        entity.setSubmittedAt(OffsetDateTime.now());

        return repository.save(entity);
    }
}
