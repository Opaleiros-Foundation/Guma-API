package university.jala.gumaapi.handler;

import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import university.jala.gumaapi.handler.exceptions.ExceptionPattern;
import university.jala.gumaapi.handler.exceptions.LessonNotFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class HandlerException {
    @ExceptionHandler(LessonNotFoundException.class)
    public ResponseEntity<ExceptionPattern> handlerUserNotFoundException(LessonNotFoundException exception) {
        LocalDateTime timestamp = LocalDateTime.now();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ExceptionPattern.builder()
                        .title("Lesson Not Found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .details(exception.getMessage())
                        .timestamp(timestamp)
                        .developerMessage(exception.getClass().getName())
                        .build()
        );
    }

    @ExceptionHandler(InvalidContentTypeException.class)
    public ResponseEntity<ExceptionPattern> handlerUserNotFoundException(InvalidContentTypeException exception) {
        LocalDateTime timestamp = LocalDateTime.now();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionPattern.builder()
                        .title("The content type is not supported")
                        .status(HttpStatus.NOT_FOUND.value())
                        .details(exception.getMessage())
                        .timestamp(timestamp)
                        .developerMessage("It must be multipart/form-data")
                        .build()
        );
    }

}
