package university.jala.gumaapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import university.jala.gumaapi.dtos.request.ChatDTO;
import university.jala.gumaapi.dtos.response.ChatDTOResponse;
import university.jala.gumaapi.entity.LessonReviews;
import university.jala.gumaapi.service.ChatReviewService;
import university.jala.gumaapi.swagger.annotations.NotFoundResponse;
import university.jala.gumaapi.swagger.annotations.OkResponse;

@Tag(name = "chat")
@RestController
@RequestMapping("/chat")
@AllArgsConstructor
@Log4j2
public class ChatReviewController {
    private final ChatReviewService chatReviewService;

    @Operation(summary = "Verify Assignment", description = "Generate a prompt to verify your submission")
    @OkResponse
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatDTOResponse> verifyAssignment(
            @RequestBody ChatDTO body,
            @RequestParam int courseId,
            @RequestParam int assignmentId,
            @RequestHeader("access_token") String token
    ) {
        log.info("[CHATREVIEW CONTROLLER] (POST /chat?courseId={}&assignmentId={}) - Verifying assignment for {}", courseId, assignmentId, body);
        return this.chatReviewService.verifyAssignment(body, courseId, assignmentId, token);
    }

    @Operation(summary = "Get Assignment", description = "Get assignment answer by uuid")
    @OkResponse
    @NotFoundResponse
    @GetMapping("/{lessonId}")
    public Mono<LessonReviews> getLessonReviews(@PathVariable String lessonId) {
        return this.chatReviewService.getLessonReviewsById(lessonId);
    }

}
