package university.jala.gumaapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonReviews {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String lessonId;

    private String subject;

    private String professor;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String feedback;

    @Column(nullable = false)
    private String model;

    @CreationTimestamp
    private String responseDate;
}
