package university.jala.gumaapi.handler.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ExceptionPattern {
    private String title;
    private int status;
    private String details;
    private LocalDateTime timestamp;
    private String developerMessage;
    private String fields;
    private String fieldsMessage;
}