package com.tsg.feedbackapi.repositories;

import static org.junit.jupiter.api.Assertions.*;
import com.tsg.feedbackapi.repositories.entities.FeedbackEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class FeedbackRepoTest {

    @Autowired
    private FeedbackRepo feedbackRepo;

    @Test
    void findByMemberId_ReturnsMatchingResults() {
        // Step 1: Create and save an entity with memberId "m-222"
        FeedbackEntity entity = new FeedbackEntity();
        entity.setId(UUID.randomUUID());
        entity.setMemberId("m-222");
        entity.setProviderName("provider");
        entity.setRating(5);
        entity.setComment("great");
        entity.setSubmittedAt(OffsetDateTime.now());

        feedbackRepo.save(entity);

        // Step 2: Call findByMemberId("m-111")
        List<FeedbackEntity> results = feedbackRepo.findByMemberId("m-222");

        // Step 3: Assert a single result exists
        assertEquals(1, results.size());

        // Step 4: Assert returned entity contains correct fields
        assertThat(results.get(0).getMemberId()).isEqualTo("m-222");
        assertThat(results.get(0).getRating()).isEqualTo(5);
    }


    @Test
    void findByMemberId_ReturnsEmptyList_WhenNoMatch() {
        // Step 1: Ensure repo contains no matching memberId
        // Step 2: Call repo with a memberId that doesn't exist
        List<FeedbackEntity> results = feedbackRepo.findByMemberId("does-not-exist");

        // Step 3: Assert empty list
        assertThat(results).isEmpty();
    }
}
