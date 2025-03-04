package university.jala.gumaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.jala.gumaapi.entity.LessonReviews;

public interface LessonReviewsRepository extends JpaRepository<LessonReviews, String> {
}
