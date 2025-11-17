package com.tsg.feedbackapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsg.feedbackapi.dtos.FeedbackRequestDTO;
import com.tsg.feedbackapi.dtos.FeedbackResponseDTO;
import com.tsg.feedbackapi.mappers.FeedbackMapper;
import com.tsg.feedbackapi.repositories.entities.FeedbackEntity;
import com.tsg.feedbackapi.services.FeedbackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FeedbackController.class)
@AutoConfigureMockMvc
class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackService feedbackService;

    @MockBean
    private FeedbackMapper feedbackMapper;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void submitFeedback_ReturnsCreated_WhenValid() throws Exception {

//  Step 1: Build a valid FeedbackRequestDTO JSON.
        FeedbackRequestDTO request = new FeedbackRequestDTO();

        request.setMemberId("m-222");
        request.setRating(5);
        request.setProviderName("provider");
        request.setComment("comment");

        FeedbackEntity saved = new FeedbackEntity();

        saved.setId(UUID.randomUUID());
        saved.setMemberId("m-222");
        saved.setRating(5);
        saved.setProviderName("provider");
        saved.setComment("Great service");
        saved.setSubmittedAt(OffsetDateTime.now());

        FeedbackResponseDTO response = new FeedbackResponseDTO(
                saved.getId(),
                saved.getMemberId(),
                saved.getProviderName(),
                saved.getSubmittedAt(),
                saved.getRating(),
                saved.getComment()
        );

        String json = """
                {
                  "memberId": "123",
                  "providerName": "Provider X",
                  "rating": 5,
                  "comment": "Great service"
                }
                """;

//  Step 2: Mock feedbackService.saveFeedback(...) to return a non-null FeedbackEntity.

        when(feedbackService.saveFeedback(any())).thenReturn(saved);
//  Step 3: Mock mapper.toResponse(...) to return a FeedbackResponseDTO.
        when(feedbackMapper.toResponse(saved)).thenReturn(response);
//  Step 4: Call POST /api/v1/feedback with valid JSON using MockMvc.
        mockMvc.perform(post("/api/v1/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.memberId").value("m-222"))
                .andExpect(jsonPath("$.providerName").value("provider"))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.comment").value("Great service"));

//  Step 5: Expect status 201 CREATED.
//  Step 6: Expect JSON response to contain the mapped fields

    }

    @Test
    void getFeedbackEntity_Returns200_WhenFound() throws Exception {
//         Step 1: Prepare a valid UUID.
        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

// Step 2: Build a FeedbackEntity returned by the service.
        FeedbackEntity entity = new FeedbackEntity();
        entity.setId(id);
        entity.setMemberId("m-222");
        entity.setProviderName("provider");
        entity.setRating(5);
        entity.setComment("Great service!");
        entity.setSubmittedAt(OffsetDateTime.now());

// Step 3: Build the mapped ResponseDTO.
        FeedbackResponseDTO response = new FeedbackResponseDTO(
                entity.getId(),
                entity.getMemberId(),
                entity.getProviderName(),
                entity.getSubmittedAt(),
                entity.getRating(),
                entity.getComment()
        );

// Step 4: Mock service + mapper.
        when(feedbackService.getFeedbackById(id)).thenReturn(entity);
        when(feedbackMapper.toResponse(entity)).thenReturn(response);

// Step 5: Call GET /api/v1/feedback/{id} and assert 200 + JSON.
        mockMvc.perform(get("/api/v1/feedback/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.memberId").value("m-222"))
                .andExpect(jsonPath("$.providerName").value("provider"))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.comment").value("Great service!"));
    }


    @Test
    void getByMemberId_Returns200_WithList() throws Exception {

// Step 1: Create a mock FeedbackEntity representing a saved record.
        UUID id = UUID.randomUUID();

        FeedbackEntity entity = new FeedbackEntity();
        entity.setId(id);
        entity.setMemberId("m-222");
        entity.setProviderName("provider");
        entity.setRating(5);
        entity.setComment("comment");
        entity.setSubmittedAt(OffsetDateTime.now());

// Step 2: Create the mapped FeedbackResponseDTO that the mapper should return.
        FeedbackResponseDTO dto = new FeedbackResponseDTO(
                id,
                "m-222",
                "provider",
                entity.getSubmittedAt(),
                5,
                "comment"
        );

// Step 3: Mock the service to return a list with one entity.
        when(feedbackService.getFeedbackByMemberId("m-222"))
                .thenReturn(List.of(entity));

// Step 4: Mock the mapper to convert the entity into its DTO.
        when(feedbackMapper.toResponse(entity)).thenReturn(dto);

// Step 5: Call GET /api/v1/feedback and pass memberId=m-222.
        mockMvc.perform(get("/api/v1/feedback?memberId=m-222"))

// Step 6: Expect status 200 OK.
                .andExpect(status().isOk())

// Step 7: Validate the returned JSON list contains the correct values.
                .andExpect(jsonPath("$[0].memberId").value("m-222"))
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$.length()").value(1));
    }


//    BAD REQUESTS


    @Test
    void submitFeedback_Returns400_WhenServiceReturnsNull() throws Exception {
        FeedbackRequestDTO request = new FeedbackRequestDTO();
        request.setMemberId("12345");
        request.setProviderName("Provider");
        request.setRating(5);
        request.setComment("Great!");

        when(feedbackService.saveFeedback(any())).thenReturn(null);

        mockMvc.perform(post("/api/v1/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void getFeedbackEntity_Returns404_WhenNotFound() throws Exception {
// Step 1: Build a valid FeedbackResponseDTO JSON.

        FeedbackRequestDTO request = new FeedbackRequestDTO();

        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        request.setMemberId("m-222");
        request.setRating(5);
        request.setProviderName("provider");
        request.setComment("comment");

// Step 2: Mock feedbackService.saveFeedback(...) to return null.
        when(feedbackService.getFeedbackById(id)).thenReturn(null);
        mockMvc.perform(get("/api/v1/feedback/" + id)
                        .accept(MediaType.APPLICATION_JSON))
//         Step 4: Expect status 400 BAD REQUEST.
                .andExpect(status().isNotFound())
//         Step 5: No body returned.
                .andExpect(content().string(""));
    }


}