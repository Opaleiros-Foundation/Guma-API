package university.jala.gumaapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LessonReviews {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String lessonId;

    private String subject;

    private String professor;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String feedback;

    @CreationTimestamp
    private String responseDate;
}
