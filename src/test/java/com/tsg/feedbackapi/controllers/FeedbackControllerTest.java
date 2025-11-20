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

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void submitFeedback_ReturnsCreated_WhenValid() throws Exception {
        // Arrange: Build valid request DTO and mock service & mapper
        FeedbackRequestDTO request = new FeedbackRequestDTO();
        request.setMemberId("m-222");
        request.setRating(5);
        request.setProviderName("provider");
        request.setComment("comment");

        FeedbackResponseDTO saved = new FeedbackResponseDTO();
        saved.setId(UUID.randomUUID());
        saved.setMemberId("m-222");
        saved.setRating(5);
        saved.setProviderName("provider");
        saved.setComment("Great service");
        saved.setSubmittedAt(OffsetDateTime.now());

        when(feedbackService.saveFeedback(any())).thenReturn(saved);

        // Act: Perform POST request via MockMvc
        mockMvc.perform(post("/api/v1/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Assert: Verify response status and JSON content
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.memberId").value("m-222"))
                .andExpect(jsonPath("$.providerName").value("provider"))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.comment").value("Great service"));
    }

    @Test
    void getFeedbackEntity_Returns200_WhenFound() throws Exception {
        // Arrange: Prepare UUID, FeedbackEntity, ResponseDTO, and mock service & mapper
        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        FeedbackResponseDTO entity = new FeedbackResponseDTO();
        entity.setId(id);
        entity.setMemberId("m-222");
        entity.setProviderName("provider");
        entity.setRating(5);
        entity.setComment("Great service!");
        entity.setSubmittedAt(OffsetDateTime.now());

        when(feedbackService.getFeedbackById(id)).thenReturn(entity);

        // Act & Assert: Call GET and verify status + JSON
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
        // Arrange: Create mock entity and corresponding DTO, mock service and mapper
        UUID id = UUID.randomUUID();

        FeedbackResponseDTO entity = new FeedbackResponseDTO();
        entity.setId(id);
        entity.setMemberId("m-222");
        entity.setProviderName("provider");
        entity.setRating(5);
        entity.setComment("comment");
        entity.setSubmittedAt(OffsetDateTime.now());


        when(feedbackService.getFeedbackByMemberId("m-222")).thenReturn(List.of(entity));

        // Act & Assert: Perform GET request and verify response
        mockMvc.perform(get("/api/v1/feedback?memberId=m-222"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].memberId").value("m-222"))
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$.length()").value(1));
    }


//    BAD REQUESTS


    @Test
    void submitFeedback_Returns400_WhenServiceReturnsNull() throws Exception {
        // Arrange: Build valid request DTO and mock service to return null
        FeedbackRequestDTO request = new FeedbackRequestDTO();
        request.setMemberId("12345");
        request.setProviderName("Provider");
        request.setRating(5);
        request.setComment("Great!");

        when(feedbackService.saveFeedback(any())).thenReturn(null);

        // Act & Assert: Perform POST request and expect 400 Bad Request
        mockMvc.perform(post("/api/v1/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void getFeedbackEntity_Returns404_WhenNotFound() throws Exception {
        // Arrange: Prepare UUID and mock service to return null
        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(feedbackService.getFeedbackById(id)).thenReturn(null);

        // Act & Assert: Perform GET request and verify 404 with empty body
        mockMvc.perform(get("/api/v1/feedback/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }


}