package university.jala.gumaapi.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDTOResponse {
    private String subject;
    private String professor;
    private String content;
    private String model;
}
