package com.tsg.feedbackapi.controllers;

import com.tsg.feedbackapi.dtos.FeedbackRequestDTO;
import com.tsg.feedbackapi.dtos.FeedbackResponseDTO;
import com.tsg.feedbackapi.repositories.entities.Feedback;
import com.tsg.feedbackapi.services.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> submitFeedback(@RequestBody @Valid FeedbackRequestDTO request) {
        Feedback saved = feedbackService.saveFeedback(request);
        FeedbackResponseDTO response = mapToResponse(saved);
        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/{id}")
    public FeedbackResponseDTO getFeedback(@PathVariable UUID id) {
        Feedback entity = feedbackService.getFeedbackById(id);
        return mapToResponse(entity);
    }

    @GetMapping
    public List<FeedbackResponseDTO> getByMemberId(@RequestParam String memberId) {
        List<Feedback> entities = feedbackService.getFeedbackByMemberId(memberId);

        return entities.stream()
                .map(this::mapToResponse)
                .toList();
    }


    private FeedbackResponseDTO mapToResponse(Feedback entity) {
        return new FeedbackResponseDTO(
            entity.getMemberId(),
            entity.getProviderName(),
            entity.getSubmittedAt(),
            entity.getRating(),
            entity.getComment()
        );
    }
}
