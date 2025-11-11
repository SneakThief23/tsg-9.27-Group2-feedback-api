package com.tsg.feedbackapi.dtos;

import jakarta.validation.constraints.*;

import java.time.OffsetDateTime;

public class FeedbackResponseDTO {

    @NotNull
    private String memberId;

    @NotNull
    private String providerName;

    @NotNull
    private OffsetDateTime submittedAt;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment;

    public FeedbackResponseDTO() {}

    public FeedbackResponseDTO(String memberId, String providerName, OffsetDateTime submittedAt, Integer rating, String comment) {
        this.memberId = memberId;
        this.providerName = providerName;
        this.submittedAt = submittedAt;
        this.rating = rating;
        this.comment = comment;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public OffsetDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(OffsetDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
