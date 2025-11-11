package com.tsg.feedbackapi.mappers;


import com.tsg.feedbackapi.dtos.FeedbackDto;
import com.tsg.feedbackapi.repositories.entities.Feedback;
public class FeedbackMapper {


    //    @Id
//    @GeneratedValue
//    private UUID id;
//
//    @Column(name = "member_id")
//    private String memberId;
//
//    @Column(name = "provider_name")
//    private String providerName;
//
//    private Integer rating;
//    private String comment;
//
//    private OffsetDateTime submittedAt;
    public static FeedbackDto feedbackToDto(Feedback feedbackEntity) {

        return new FeedbackDto(
                feedbackEntity.getId(),
                feedbackEntity.getMemberId(),
                feedbackEntity.getProviderName(),
                feedbackEntity.getSubmittedAt(),
                feedbackEntity.getRating(),
                feedbackEntity.getComment()

        );
    }

    public static Feedback dtoToEntity(FeedbackDto feedbackDto) {

        Feedback feedbackEntity = new Feedback();

        feedbackEntity.setMemberId(feedbackDto.getMemberId());
        feedbackEntity.setProviderName(feedbackDto.getProviderName());
        feedbackEntity.setSubmittedAt(feedbackDto.getSubmittedAt());
        feedbackEntity.setRating(feedbackDto.getRating());
        feedbackEntity.setComment(feedbackDto.getComment());
        return feedbackEntity;

    }
}
