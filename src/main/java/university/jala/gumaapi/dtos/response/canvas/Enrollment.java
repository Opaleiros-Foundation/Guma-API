package university.jala.gumaapi.dtos.response.canvas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    public String type;
    public String role;
    @JsonProperty("role_id")
    public int roleId;
    @JsonProperty("user_id")
    public int userId;
    @JsonProperty("enrollment_state")
    public String enrollmentState;
    @JsonProperty("limit_privileges_to_course_section")
    public boolean limitPrivilegesToCourseSection;
}