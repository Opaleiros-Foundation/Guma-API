package university.jala.gumaapi.service;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import university.jala.gumaapi.dtos.request.ChatDTO;
import university.jala.gumaapi.dtos.response.ChatDTOResponse;
import university.jala.gumaapi.entity.LessonReviews;

public interface ChatReviewService {
    Flux<ChatDTOResponse> verifyAssignment(ChatDTO body, int courseId, int assignmentId, String token);
    Mono<LessonReviews> getLessonReviewsById(String lessonId);

}
