package university.jala.gumaapi.service;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import university.jala.gumaapi.dtos.request.ChatDTO;
import university.jala.gumaapi.dtos.response.ChatDTOResponse;

public interface ChatReviewService {
    Flux<ChatDTOResponse>  verifyAssignment(ChatDTO body, MultipartFile file);


}
