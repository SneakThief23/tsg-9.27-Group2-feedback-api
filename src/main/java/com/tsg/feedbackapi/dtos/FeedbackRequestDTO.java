package com.tsg.feedbackapi.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

public class FeedbackRequestDTO {

    @NotNull @Size(max = 36) private String memberId;
    @NotNull @Size(max = 80) private String providerName;
    @NotNull @Min(1) @Max(5) private Integer rating;
    @Size(max = 200) private String comment;

    public FeedbackRequestDTO() {}

    public FeedbackRequestDTO(String memberId, String providerName, Integer rating, String comment) {
        this.memberId = memberId;
        this.providerName = providerName;
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

}
//Had to remove lombok to stop FeedbackMapper errors
//From the client to the backend