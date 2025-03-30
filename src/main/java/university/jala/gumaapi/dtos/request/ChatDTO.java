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
public class ChatDTO {
    @NotBlank(message = "subject is mandatory")
    @Schema(description = "The school subject", example = "Database 2")
    private String subject;

    @NotBlank(message = "professor is mandatory")
    @Schema(description = "The professor responsible for the subject", example = "John")
    private String professor;
    @Schema(description = "The heading of the assignment", example = "Do A great database")
    private List<Rubric> heading;
}
