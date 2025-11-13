package com.tsg.feedbackapi.services;

import com.tsg.feedbackapi.dtos.FeedbackRequestDTO;
import com.tsg.feedbackapi.dtos.FeedbackResponseDTO;
import com.tsg.feedbackapi.mappers.FeedbackMapper;
import com.tsg.feedbackapi.repositories.FeedbackRepo;
import com.tsg.feedbackapi.repositories.entities.FeedbackEntity;
import com.tsg.feedbackapi.services.FeedbackService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

//         Step 1: Set up mocks for repository, KafkaTemplate, and mapper
    @Mock
    private FeedbackRepo feedbackRepo;
    @Mock
    private FeedbackMapper feedbackMapper;

    @Mock
    private KafkaTemplate<String, FeedbackResponseDTO> kafkaTemplate;


//     Step 2: Instantiate the FeedbackService using the mocks
    @InjectMocks
    private FeedbackService service;

    @Test
    void shouldSaveFeedbackSuccessfully() {
//         Step 3: Arrange a FeedbackRequestDTO with test data
        FeedbackRequestDTO feedbackRequestDTO = new FeedbackRequestDTO();
        feedbackRequestDTO.setRating(4);
        feedbackRequestDTO.setProviderName("TestProvider");
        feedbackRequestDTO.setMemberId("m-123");
        feedbackRequestDTO.setComment("Great service!");
//         Step 4: Mock repository save method to return a FeedbackEntity
        FeedbackEntity mockedEntity =  new FeedbackEntity(
                UUID.randomUUID(),
                feedbackRequestDTO.getMemberId(),
                feedbackRequestDTO.getProviderName(),
                feedbackRequestDTO.getRating(),
                feedbackRequestDTO.getComment(),
                OffsetDateTime.now()
                );

        when(feedbackRepo.save(any(FeedbackEntity.class))).thenReturn(mockedEntity);
//         Step 5: Mock mapper to convert entity to response DTO
        FeedbackResponseDTO mockedResponse = feedbackMapper.toResponse(mockedEntity);

        when(feedbackMapper.toResponse(any(FeedbackEntity.class))).thenReturn(mockedResponse);
//         Step 6: Act by calling saveFeedback() on the service
        FeedbackEntity result = service.saveFeedback(feedbackRequestDTO);
//         Step 7: Assert that returned entity matches expectations
        assertNotNull(result);
        assertEquals("m-123", result.getMemberId());
        assertEquals("TestProvider", result.getProviderName());
        assertEquals(4, result.getRating());
        assertEquals("Great service!", result.getComment());

//         Step 8: Verify KafkaTemplate send method was called with correct topic/key/value

        verify(kafkaTemplate).send(eq("feedback-submitted"), eq(mockedEntity.getId().toString()), eq(mockedResponse));
    }

    @Test
    void shouldThrowWhenFeedbackNotFoundById() {
//         Step 1: Arrange repository to return empty when findById is called

//         Step 2: Act & Assert: call getFeedbackById() and expect exception
    }

    @Test
    void shouldReturnListOfFeedbackForMember() {
//         Step 1: Arrange repository to return a list of FeedbackEntity for a memberId

//         Step 2: Act by calling getFeedbackByMemberId() on the service

//         Step 3: Assert that returned list matches expected size and content
    }

    @Test
    void shouldHandleNullEventGracefullyInListener() {
//         Step 1: Arrange a FeedbackEventListener instance

//         Step 2: Act by calling onMessage(null)

//         Step 3: Assert that method returns null and does not throw an exception
    }

    @Test
    void shouldProcessEventAndReturnStringRepresentation() {
//         Step 1: Arrange a FeedbackResponseDTO with sample data

//         Step 2: Act by calling onMessage() with the DTO

//         Step 3: Assert that the returned string contains all relevant fields
    }

//         Step 4: Additional tests can include validation failures, Kafka exceptions, etc.
}
