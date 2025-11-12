package com.tsg.feedbackapi.mappers;


import com.tsg.feedbackapi.repositories.entities.FeedbackEntity;
import com.tsg.feedbackapi.dtos.FeedbackResponseDTO;
import com.tsg.feedbackapi.dtos.FeedbackRequestDTO;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class FeedbackMapper {


    public FeedbackEntity dtoToEntity(FeedbackRequestDTO dto) {

        FeedbackEntity feedbackEntity = new FeedbackEntity();

        feedbackEntity.setMemberId(dto.getMemberId());
        feedbackEntity.setProviderName(dto.getProviderName());
        feedbackEntity.setRating(dto.getRating());
        feedbackEntity.setComment(dto.getComment());

        //backend sets the timestamp
        feedbackEntity.setSubmittedAt(OffsetDateTime.now());
        return feedbackEntity;

    }

    public FeedbackResponseDTO toResponse(FeedbackEntity entity) {
        return new FeedbackResponseDTO(
                entity.getId(),
                entity.getMemberId(),
                entity.getProviderName(),
                entity.getSubmittedAt(),
                entity.getRating(),
                entity.getComment()
        );
    }
}