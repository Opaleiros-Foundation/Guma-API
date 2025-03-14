package university.jala.gumaapi.service;

import org.springframework.web.multipart.MultipartFile;
import university.jala.gumaapi.dtos.request.ChatDTO;
import university.jala.gumaapi.dtos.response.ChatDTOResponse;

public interface ChatReviewService {
    ChatDTOResponse verifyAssignment(ChatDTO body, MultipartFile file);
}
