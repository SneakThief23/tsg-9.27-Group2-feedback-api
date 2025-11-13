package com.tsg.feedbackapi.services;

import com.tsg.feedbackapi.dtos.FeedbackRequestDTO;
import com.tsg.feedbackapi.dtos.FeedbackResponseDTO;
import com.tsg.feedbackapi.mappers.FeedbackMapper;
import com.tsg.feedbackapi.messaging.FeedbackEventPublisher;
import com.tsg.feedbackapi.repositories.FeedbackRepo;
import com.tsg.feedbackapi.repositories.entities.FeedbackEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
    private FeedbackService feedbackService;
    @InjectMocks
    private FeedbackEventPublisher publisher;

    @Test
    void shouldSaveFeedbackSuccessfully() {
//         Step 3: Arrange a FeedbackRequestDTO with test data
        FeedbackRequestDTO feedbackRequestDTO = new FeedbackRequestDTO();
        feedbackRequestDTO.setRating(4);
        feedbackRequestDTO.setProviderName("TestProvider");
        feedbackRequestDTO.setMemberId("m-123");
        feedbackRequestDTO.setComment("Great service!");
//         Step 4: Mock repository save method to return a FeedbackEntity
        FeedbackEntity mockedEntity = new FeedbackEntity(
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
        FeedbackEntity result = feedbackService.saveFeedback(feedbackRequestDTO);
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
        UUID id = UUID.randomUUID();
        when(feedbackRepo.findById(id)).thenReturn(Optional.empty());
//         Step 2: Act & Assert: call getFeedbackById() and expect exception
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> feedbackService.getFeedbackById(id)
        );

        assertEquals("Feedback not found: " + id, exception.getMessage());
        verify(feedbackRepo, times(1)).findById(id);

    }

    @Test
    void shouldReturnListOfFeedbackForMember() {
//         Step 1: Arrange repository to return a list of FeedbackEntity for a memberId
        String memberId = "m-123";

        FeedbackEntity feedback1 = new FeedbackEntity();
        feedback1.setMemberId("m-123");
        feedback1.setComment("Great service!");

        FeedbackEntity feedback2 = new FeedbackEntity();
        feedback2.setMemberId("m-123");
        feedback2.setComment("Very good");

        List<FeedbackEntity> feedbackList = List.of(feedback1, feedback2);

//         Step 2: Act by calling getFeedbackByMemberId() on the service
        when(feedbackRepo.findByMemberId(memberId)).thenReturn(feedbackList);
        List<FeedbackEntity> result = feedbackService.getFeedbackByMemberId(memberId);

//         Step 3: Assert that returned list matches expected size and content
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(feedbackRepo, times(1)).findByMemberId(memberId);
    }
}
