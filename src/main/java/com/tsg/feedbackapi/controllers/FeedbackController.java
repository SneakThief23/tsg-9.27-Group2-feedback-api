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
import java.util.Map;
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


    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
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
    public ResponseEntity<FeedbackResponseDTO> submitFeedback( @Valid @RequestBody FeedbackRequestDTO request) {
        System.out.println("FeedbackController hit");
        FeedbackResponseDTO saved = feedbackService.saveFeedback(request);
        if (saved == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
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
        FeedbackResponseDTO entityDto = feedbackService.getFeedbackById(id);
        if (entityDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entityDto);
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
    public ResponseEntity<List<FeedbackResponseDTO>> getByMemberId(@RequestParam String memberId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByMemberId(memberId));
    }
}