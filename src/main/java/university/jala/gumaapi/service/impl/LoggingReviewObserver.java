package university.jala.gumaapi.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import university.jala.gumaapi.entity.LessonReviews;
import university.jala.gumaapi.service.ReviewObserver;

@Component
@Log4j2
public class LoggingReviewObserver implements ReviewObserver {
    @Override
    public void onReviewSaved(LessonReviews review) {
        log.info("Nova revisão salva: {}", review.getLessonId());
    }
}
