package university.jala.gumaapi.dtos.response.canvas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    public String id;
    public double points;
    public String description;
    public String long_description;
}