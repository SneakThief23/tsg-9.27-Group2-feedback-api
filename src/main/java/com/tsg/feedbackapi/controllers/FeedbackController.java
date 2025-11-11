package com.tsg.feedbackapi.controllers;

import com.tsg.feedbackapi.dtos.FeedbackDto;
import com.tsg.feedbackapi.dtos.FeedbackResponseDTO;
import com.tsg.feedbackapi.mappers.FeedbackMapper;
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
    public ResponseEntity<FeedbackDto> submitFeedback(@RequestBody @Valid FeedbackDto request) {
        Feedback saved = feedbackService.saveFeedback(request);
        return ResponseEntity.accepted().body(FeedbackMapper.feedbackToDto(saved));
    }


    @GetMapping("/{id}")
    public FeedbackDto getFeedback(@PathVariable UUID id) {
        Feedback entity = feedbackService.getFeedbackById(id);
        return FeedbackMapper.feedbackToDto(entity);
    }

    @GetMapping
    public List<FeedbackDto> getByMemberId(@RequestParam String memberId) {
        List<Feedback> entities = feedbackService.getFeedbackByMemberId(memberId);

        return entities.stream()
                .map((en) -> FeedbackMapper.feedbackToDto(en))
                .toList();
    }
}
