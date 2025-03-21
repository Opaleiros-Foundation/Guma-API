package university.jala.gumaapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import university.jala.gumaapi.entity.LessonReviews;
import university.jala.gumaapi.handler.exceptions.LessonNotFoundException;
import university.jala.gumaapi.repository.LessonReviewsRepository;
import university.jala.gumaapi.service.LessonReviewsService;

@Service
@RequiredArgsConstructor
public class LessonReviewsServiceImpl implements LessonReviewsService {

    private final LessonReviewsRepository service;

    @Override
    public LessonReviews findByLessonReviewId(String lessonId) {
        return service.findById(lessonId).orElseThrow(() -> new LessonNotFoundException("Lesson review not found with uuid: " + lessonId));
    }

    @Transactional
    @Override
    public void saveLessonReview(LessonReviews lessonReview) {
        service.save(lessonReview);
    }
}
