package com.tsg.feedbackapi.controllers;

import com.tsg.feedbackapi.dtos.FeedbackRequestDTO;
import com.tsg.feedbackapi.dtos.FeedbackResponseDTO;
import com.tsg.feedbackapi.mappers.FeedbackMapper;
import com.tsg.feedbackapi.repositories.entities.FeedbackEntity;
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
    private final FeedbackMapper mapper;

    public FeedbackController(FeedbackService feedbackService, FeedbackMapper mapper) {
        this.feedbackService = feedbackService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> submitFeedback(@Valid @RequestBody FeedbackRequestDTO request) {
        FeedbackEntity saved = feedbackService.saveFeedback(request);
        FeedbackResponseDTO response = mapper.toResponse(saved);
        return ResponseEntity.accepted().body(response);
    }


    @GetMapping("/{id}")
    public FeedbackResponseDTO getFeedbackEntity(@PathVariable UUID id) {
        FeedbackEntity entity = feedbackService.getFeedbackById(id);
        return mapper.toResponse(entity);
    }

    @GetMapping
    public List<FeedbackResponseDTO> getByMemberId(@RequestParam String memberId) {
        List<FeedbackEntity> entities = feedbackService.getFeedbackByMemberId(memberId);

        return entities.stream()
                .map(en -> mapper.toResponse(en))
                .toList();
    }
}

//added mapper reference