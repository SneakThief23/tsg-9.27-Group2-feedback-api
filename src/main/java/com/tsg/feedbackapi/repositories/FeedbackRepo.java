package com.tsg.feedbackapi.repositories;

import com.tsg.feedbackapi.repositories.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FeedbackRepo extends JpaRepository <Feedback, UUID> {
    Optional<Feedback> findByMemberId(String memberId);
}
