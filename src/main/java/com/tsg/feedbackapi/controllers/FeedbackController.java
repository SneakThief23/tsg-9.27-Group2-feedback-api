package com.tsg.feedbackapi.controllers;

import com.tsg.feedbackapi.dtos.FeedbackRequestDTO;
import com.tsg.feedbackapi.dtos.FeedbackResponseDTO;
import com.tsg.feedbackapi.mappers.FeedbackMapper;
import com.tsg.feedbackapi.repositories.entities.FeedbackEntity;
import com.tsg.feedbackapi.services.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "http://localhost:5173")
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
        if(saved == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        FeedbackResponseDTO response = mapper.toResponse(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> getFeedbackEntity(@PathVariable UUID id) {
        FeedbackEntity entity = feedbackService.getFeedbackById(id);
        if(entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toResponse(entity));
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