package com.tsg.feedbackapi.controllers;

import com.tsg.feedbackapi.dtos.FeedbackRequest;
import com.tsg.feedbackapi.dtos.FeedbackResponse;
import com.tsg.feedbackapi.repositories.entities.FeedbackEntity;
import com.tsg.feedbackapi.services.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<FeedbackResponse> submitFeedback(@RequestBody @Valid FeedbackRequest request) {
        FeedbackEntity saved = feedbackService.saveFeedback(request);
        FeedbackResponse response = mapToResponse(saved);
        return ResponseEntity.accepted().body(response);
    }

    private FeedbackResponse mapToResponse(FeedbackEntity entity) {
        return new FeedbackResponse(
            entity.getMemberId(),
            entity.getProviderName(),
            entity.getSubmittedAt(),
            entity.getRating(),
            entity.getComment()
        );
    }
}
