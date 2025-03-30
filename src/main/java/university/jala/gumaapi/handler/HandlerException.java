package university.jala.gumaapi.handler;

import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import university.jala.gumaapi.handler.exceptions.ExceptionPattern;
import university.jala.gumaapi.handler.exceptions.LessonNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionPattern> handlerMethodArgumentNotValidExceptionException(MethodArgumentNotValidException exception) {
        List<FieldError> fieldError = exception.getBindingResult().getFieldErrors();

        String fields = fieldError.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldMessage = fieldError.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        LocalDateTime timestamp = LocalDateTime.now();

        return ResponseEntity.badRequest().body(
                ExceptionPattern.builder()
                        .title("Bad Request Exception, Invalid Fields")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .details("Check the field(s) error")
                        .timestamp(timestamp)
                        .developerMessage(exception.getClass().getName())
                        .fields(fields)
                        .fieldsMessage(fieldMessage)
                        .build()
        );
    }

}
