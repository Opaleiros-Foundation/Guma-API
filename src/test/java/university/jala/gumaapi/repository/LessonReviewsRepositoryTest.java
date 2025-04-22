package university.jala.gumaapi.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import university.jala.gumaapi.entity.LessonReviews;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Lesson Review Repository")
class LessonReviewsRepositoryTest {

    @Autowired
    private LessonReviewsRepository lessonReviewsRepository;

    @AfterEach
    void tearDown() {
        lessonReviewsRepository.deleteAll();
    }

    @Test
    @DisplayName("Should get lesson reviews by id successfully")
    public void should_get_lesson_reviews_by_id_successfully() {
        Optional<LessonReviews> lessonById = this.lessonReviewsRepository.findById("1");
        assertNotNull(lessonById);
    }
}