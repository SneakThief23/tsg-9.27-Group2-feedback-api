package com.tsg.feedbackapi.repo;

import com.tsg.feedbackapi.repositories.FeedbackRepo;
import com.tsg.feedbackapi.repositories.entities.FeedbackEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect"
})

//Tests will not work without changing the FeedbackEntity and I don't
//want to break anything right now.
public class FeedbackRepoTest {

    @Autowired
    private FeedbackRepo feedbackRepo;

    @Test
    void shouldSaveAndFindFeedbackByMemberId() {

        //arrange the data to be mocked
        FeedbackEntity entity1 = new FeedbackEntity();
        entity1.setMemberId("m-123");
        entity1.setComment("neat");
        entity1.setRating(2);
        entity1.setProviderName("Dr. Provider");

        FeedbackEntity entity2 = new FeedbackEntity();
        entity2.setMemberId("m-123");
        entity2.setComment("cool");
        entity2.setRating(4);
        entity2.setProviderName("Dr. Doctor");

        feedbackRepo.save(entity1);
        feedbackRepo.save(entity2);

        //act
        List<FeedbackEntity> results = feedbackRepo.findByMemberId("m-123");

        //assert
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(f -> f.getComment().equals("neat")));
        assertTrue(results.stream().anyMatch(f -> f.getComment().equals("cool")));
    }

    @Test
    void shouldReturnEmptyListIfNoFeedbackForMember() {
        List<FeedbackEntity> results = feedbackRepo.findByMemberId("nonexistent-member");
        assertTrue(results.isEmpty());
    }
}
