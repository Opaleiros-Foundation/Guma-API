package university.jala.gumaapi.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import university.jala.gumaapi.dtos.request.ChatDTO;
import university.jala.gumaapi.dtos.response.ChatDTOResponse;
import university.jala.gumaapi.service.ChatReviewService;

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
@Log4j2
public class ChatReviewController {
    private final ChatReviewService chatReviewService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatDTOResponse> verifyAssignment(@RequestPart("body") ChatDTO body, @RequestPart("file") MultipartFile file) {
        log.info("Verifying assignment for {}", body);
        return this.chatReviewService.verifyAssignment(body, file);
    }

}
