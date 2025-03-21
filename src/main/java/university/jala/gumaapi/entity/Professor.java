package university.jala.gumaapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;
    private String name;
}
