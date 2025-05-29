package university.jala.gumaapi.dtos.response.canvas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Rubric {
    public String id;
    public int points;
    public String description;
    @JsonProperty("long_description")
    public Object longDescription;
    @JsonProperty("ignore_for_scoring")
    public Object ignoreForScoring;
    @JsonProperty("criterion_use_range")
    public boolean criterionUseRange;
    public ArrayList<Rating> ratings;
}
