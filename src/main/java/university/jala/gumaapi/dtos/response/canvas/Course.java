package university.jala.gumaapi.dtos.response.canvas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    public int id;
    public String name;
    public int account_id;
    public String uuid;
    @JsonProperty("course_code")
    public String courseCode;
    @JsonProperty("default_view")
    public String defaultView;
    @JsonProperty("root_account_id")
    public int rootAccountId;
    @JsonProperty("enrollment_term_id")
    public int enrollmentTermId;
    public String license;
    @JsonProperty("public_syllabus_to_auth")
    public boolean publicSyllabusToAuth;
    @JsonProperty("storage_quota_mb")
    public int storageQuotaMb;
    @JsonProperty("is_public_to_auth_users")
    public boolean isPublicToAuthUsers;
    public ArrayList<Enrollment> enrollments;
}