/*
Split into the 2 DTOs instead of one.
One for requesting data, one for returning

package com.tsg.feedbackapi.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {

    private UUID id;

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

    public FeedbackDto() {
    }

    public FeedbackDto(UUID id, String memberId, String providerName, OffsetDateTime submittedAt, Integer rating, String comment) {
        this.id = id;
        this.memberId = memberId;
        this.providerName = providerName;
        this.submittedAt = submittedAt;
        this.rating = rating;
        this.comment = comment;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setSubmittedAt(OffsetDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}

*/