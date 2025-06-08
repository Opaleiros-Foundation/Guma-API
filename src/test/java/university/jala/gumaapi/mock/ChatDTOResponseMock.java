package university.jala.gumaapi.mock;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import university.jala.gumaapi.dtos.request.ChatDTO;
import university.jala.gumaapi.dtos.response.ChatDTOResponse;

public class ChatDTOResponseMock {
    public static Flux<ChatDTOResponse> mockChatDTO() {
        ChatDTOResponse response = ChatDTOResponse.builder()
                .model("model")
                .subject("subject")
                .professor("professor")
                .build();

        return Flux.just(response);
    }

    public static ChatDTO returnValidChatDTO() {
        return ChatDTO.builder()
                .subject("subject")
                .content("21")
                .professor("jose")
                .build();

    }
}
