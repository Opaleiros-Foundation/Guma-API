package university.jala.gumaapi.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import university.jala.gumaapi.service.ChatReviewService;

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatReviewController {
    private final ChatReviewService chatReviewService;
}
