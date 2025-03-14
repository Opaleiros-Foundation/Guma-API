package university.jala.gumaapi.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import university.jala.gumaapi.dtos.request.ChatDTO;
import university.jala.gumaapi.dtos.response.ChatDTOResponse;
import university.jala.gumaapi.service.ChatReviewService;

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
@Log4j2
public class ChatReviewController {
    private final ChatReviewService chatReviewService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ChatDTOResponse> verifyAssignment(@RequestPart("body") ChatDTO body, @RequestPart("file") MultipartFile file) {
        log.info("Verifying assignment for {}", body);
        ChatDTOResponse chatResponse = this.chatReviewService.verifyAssignment(body, file);
        return ResponseEntity.ok(chatResponse);
    }
}
