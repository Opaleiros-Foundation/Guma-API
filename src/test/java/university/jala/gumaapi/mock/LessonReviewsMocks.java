package university.jala.gumaapi.mock;

import reactor.core.publisher.Mono;
import university.jala.gumaapi.entity.LessonReviews;

public class LessonReviewsMocks {
    public static Mono<LessonReviews> mockLessonReviews() {
        LessonReviews lesson = LessonReviews.builder()
                .lessonId("312")
                .model("model")
                .subject("subject")
                .professor("professor")
                .build();

        return Mono.just(lesson);
    }
}
