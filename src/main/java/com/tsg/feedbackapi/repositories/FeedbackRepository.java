package com.tsg.feedbackapi.repositories;

import com.tsg.feedbackapi.repositories.entities.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository <FeedbackEntity, UUID> {
    Optional<FeedbackEntity> findByMemberId(String memberId);
}
