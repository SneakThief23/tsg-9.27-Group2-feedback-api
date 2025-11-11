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
    public static FeedbackDto feedbackToDto(Feedback feedback) {

        return new FeedbackDto(
                feedback.getId(),
                feedback.getMemberId(),
                feedback.getProviderName(),
                feedback.getSubmittedAt(),
                feedback.getRating(),
                feedback.getComment()

        );
    }

    public static Feedback dtoToEntity(FeedbackDto feedbackDto) {

        Feedback feedback = new Feedback();

        feedback.setMemberId(feedbackDto.getMemberId());
        feedback.setProviderName(feedbackDto.getProviderName());
        feedback.setSubmittedAt(feedbackDto.getSubmittedAt());
        feedback.setRating(feedbackDto.getRating());
        feedback.setComment(feedbackDto.getComment());
        return feedback;

    }
}
