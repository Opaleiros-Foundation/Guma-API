package university.jala.gumaapi.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import university.jala.gumaapi.dtos.request.ChatDTO;
import university.jala.gumaapi.dtos.response.ChatDTOResponse;
import university.jala.gumaapi.entity.LessonReviews;
import university.jala.gumaapi.service.ChatReviewService;
import university.jala.gumaapi.service.FileProcessingService;
import university.jala.gumaapi.service.LessonReviewsService;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ChatReviewServiceImpl implements ChatReviewService {
    private final ChatClient chatClient;
    private final FileProcessingService fileProcessingService;
    private final LessonReviewsService service;

    @Value("${spring.ai.ollama.chat.model}")
    private String ollamaModel;

    public ChatReviewServiceImpl(ChatClient chatClient, FileProcessingService fileProcessingService, LessonReviewsServiceImpl service) {
        this.chatClient = chatClient;
        this.fileProcessingService = fileProcessingService;
        this.service = service;
    }

    @Override
    public Flux<ChatDTOResponse> verifyAssignment(ChatDTO body, MultipartFile file) {
        String processedFile = this.fileProcessingService.processFile(file);

        StringBuilder finalResponseBuilder = new StringBuilder();

        return this.chatClient.prompt()
                .system(sp -> sp.params(Map.of(
                        "teacher", body.getProfessor(),
                        "class", body.getSubject(),
                        "heading", body.getHeading()
                )))
                .advisors(new SimpleLoggerAdvisor())
                .user("Responda o que está faltando no seguinte documento: " + processedFile)
                .stream()
                .content()
                .map(data -> {
                    finalResponseBuilder.append(data);
                    return ChatDTOResponse.builder()
                            .content(data)
                            .model(ollamaModel)
                            .thoughts(data)
                            .heading(body.getHeading())
                            .subject(body.getSubject())
                            .professor(body.getProfessor())
                            .build();
                })
                .publishOn(Schedulers.boundedElastic())
                .doOnComplete(() -> {
                    if (!finalResponseBuilder.isEmpty()) {
                        service.saveLessonReview(LessonReviews.builder()
                                .feedback(finalResponseBuilder.toString())
                                .subject(body.getSubject())
                                .professor(body.getProfessor())
                                .build());
                    }
                })
                .subscribeOn(Schedulers.parallel());
    }


}

