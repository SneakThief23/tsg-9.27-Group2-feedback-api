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

    private int rating;
    private String comment;

    @Column(name = "submitted_at", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime submittedAt;
}