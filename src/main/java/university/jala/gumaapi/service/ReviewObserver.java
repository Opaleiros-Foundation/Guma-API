package university.jala.gumaapi.service;

import university.jala.gumaapi.entity.LessonReviews;

public interface ReviewObserver {
    void onReviewSaved(LessonReviews review);
}
