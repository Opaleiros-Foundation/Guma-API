package university.jala.gumaapi.service;

import university.jala.gumaapi.entity.LessonReviews;

public interface LessonReviewsService {
    LessonReviews findByLessonReviewId(String lessonId);
    void saveLessonReview(LessonReviews lessonReview);
}
