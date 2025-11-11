package com.tsg.feedbackapi.repositories;

import com.tsg.feedbackapi.repositories.entities.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FeedbackRepo extends JpaRepository <FeedbackEntity, UUID> {
    List<FeedbackEntity> findByMemberId(String memberId);
}
