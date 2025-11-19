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

//Swagger-ui
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;


@Tag(name = "Feedback", description = "Operations related to user feedback")
@RestController
@RequestMapping("/api/v1/feedback")
@CrossOrigin(origins = "http://localhost:5173")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final FeedbackMapper mapper;


    public FeedbackController(FeedbackService feedbackService, FeedbackMapper mapper) {
        this.feedbackService = feedbackService;
        this.mapper = mapper;
    }

    @Operation(
            summary = "Submit new feedback",
            description = "Creates a new feedback entry and returns the saved object.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Feedback created",
                            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> submitFeedback(@RequestBody FeedbackRequestDTO request) {
        FeedbackEntity saved = feedbackService.saveFeedback(request);
        if (saved == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        FeedbackResponseDTO response = mapper.toResponse(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get feedback by ID",
            description = "Returns a single feedback entry by its UUID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Feedback found",
                            content = @Content(schema = @Schema(implementation = FeedbackResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> getFeedbackEntity(@PathVariable UUID id) {
        FeedbackEntity entity = feedbackService.getFeedbackById(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapper.toResponse(entity));
    }

    @Operation(
            summary = "Get feedback by memberId",
            description = "Returns all feedback entries for a given memberId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Feedback list returned",
                            content = @Content(array = @ArraySchema(
                                    schema = @Schema(implementation = FeedbackResponseDTO.class)
                            )))
            }
    )
    @GetMapping
    public List<FeedbackResponseDTO> getByMemberId(@RequestParam String memberId) {
        List<FeedbackEntity> entities = feedbackService.getFeedbackByMemberId(memberId);

        return entities.stream()
                .map(en -> mapper.toResponse(en))
                .toList();
    }
}

//added mapper reference