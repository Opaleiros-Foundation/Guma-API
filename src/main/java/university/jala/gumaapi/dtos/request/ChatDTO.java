package university.jala.gumaapi.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import university.jala.gumaapi.dtos.response.canvas.Rubric;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ChatDTO {
    @NotBlank(message = "subject is mandatory")
    @Schema(description = "The school subject", example = "Database 2")
    private String subject;

    @NotBlank(message = "professor is mandatory")
    @Schema(description = "The professor responsible for the subject", example = "John")
    private String professor;

    @NotBlank(message = "content is mandatory")
    @Schema(description = "Assignment content", example = "make a database with java")
    private String content;
    @Schema(description = "The heading of the assignment", example = "Do A great database")
    private List<Rubric> heading;
}
