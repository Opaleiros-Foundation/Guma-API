package university.jala.gumaapi.dtos.response.canvas;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rating {
    public String id;
    public double points;
    public String description;
    public String long_description;
}