package university.jala.gumaapi.mock;

import university.jala.gumaapi.dtos.request.ChatDTO;

import java.util.List;

public class ChatDTOMock {
    public static ChatDTO createAValidChatDTO() {
        return ChatDTO.builder()
                .heading(List.of())
                .content("Just do it")
                .professor("John Doe")
                .subject("Database 2")
                .build();
    }
}
