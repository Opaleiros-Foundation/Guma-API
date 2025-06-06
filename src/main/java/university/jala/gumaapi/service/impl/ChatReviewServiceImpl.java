package university.jala.gumaapi.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import university.jala.gumaapi.dtos.request.ChatDTO;
import university.jala.gumaapi.dtos.response.ChatDTOResponse;
import university.jala.gumaapi.dtos.response.canvas.Assignment;
import university.jala.gumaapi.entity.LessonReviews;
import university.jala.gumaapi.service.CanvasService;
import university.jala.gumaapi.service.ChatReviewService;
import university.jala.gumaapi.service.LessonReviewsService;
import university.jala.gumaapi.service.ReviewObserver;

import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class ChatReviewServiceImpl implements ChatReviewService {
    private final ChatClient chatClient;
    private final LessonReviewsService service;
    private final List<ReviewObserver> observers;
    private final CanvasService canvasService;


    @Value("${spring.ai.ollama.chat.model}")
    private String ollamaModel;

    public ChatReviewServiceImpl(
            ChatClient chatClient,
            LessonReviewsServiceImpl service,
            List<ReviewObserver> observers,
            CanvasService canvasService
    ) {
        this.chatClient = chatClient;
        this.service = service;
        this.observers = observers;
        this.canvasService = canvasService;
    }

    @Override
    public Flux<ChatDTOResponse> verifyAssignment(
            ChatDTO body,
            int courseId,
            int assignmentId,
            String token
    ) {

        StringBuilder finalResponseBuilder = new StringBuilder();

        Assignment assignmentFound = this.canvasService.getAssignmentById(courseId, assignmentId, token);
        body.setHeading(assignmentFound.getRubric());
        log.info("[CHATREVIEW-SERVICE] Rubric {} of assignment {}", body.getHeading(), assignmentId);

        return this.getPrompt(body)
                .map(data -> {
                    finalResponseBuilder.append(data);
                    return ChatDTOResponse.builder()
                            .content(data)
                            .model(ollamaModel)
                            .subject(body.getSubject())
                            .professor(body.getProfessor())
                            .build();
                })
                .publishOn(Schedulers.boundedElastic())
                .doOnComplete(() -> {
                    if (!finalResponseBuilder.isEmpty()) {
                        LessonReviews review = LessonReviews.builder()
                                .feedback(finalResponseBuilder.toString())
                                .subject(body.getSubject())
                                .professor(body.getProfessor())
                                .model(ollamaModel)
                                .build();
                        service.saveLessonReview(review);
                        notifyObservers(review);
                    }
                })
                .subscribeOn(Schedulers.parallel());
    }

    @Override
    public Mono<LessonReviews> getLessonReviewsById(String lessonId) {
        return Mono.just(this.service.findByLessonReviewId(lessonId));
    }

    private Flux<String> getPrompt(ChatDTO body) {
        log.info("Generating prompt to {}", body.getSubject());
        return this.chatClient.prompt()
                .system(sp -> sp.params(Map.of(
                        "teacher", body.getProfessor(),
                        "class", body.getSubject(),
                        "heading", body.getHeading()
                )))
                .user("Responda o que está faltando no seguinte documento: " + body.getContent())
                .stream()
                .content();
    }

    private void notifyObservers(LessonReviews review) {
        observers.forEach(observer -> observer.onReviewSaved(review));
    }

}

