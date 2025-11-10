package com.tsg.feedbackapi.repositories.entities;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.OffsetDateTime;

@Entity
@Table(name ="feedback")
public class FeedbackEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "provider_name")
    private String providerName;

    private Integer rating;
    private String comment;

    @Column(name = "submitted_at", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime submittedAt;

    public FeedbackEntity() {}

    public FeedbackEntity(UUID id, String memberId, String providerName, Integer rating, String comment, OffsetDateTime submittedAt) {
        this.id = id;
        this.memberId = memberId;
        this.providerName = providerName;
        this.rating = rating;
        this.comment = comment;
        this.submittedAt = submittedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
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