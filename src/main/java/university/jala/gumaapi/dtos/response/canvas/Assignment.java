package university.jala.gumaapi.dtos.response.canvas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {
    public int id;
    public String description;
    @JsonProperty("due_at")
    public Date dueAt;
    @JsonProperty("unlock_at")
    public Date unlockAt;
    @JsonProperty("lock_at")
    public Date lockAt;
    @JsonProperty("points_possible")
    public int pointsPossible;
    @JsonProperty("grading_type")
    public String gradingType;
    @JsonProperty("assignment_group_id")
    public int assignmentGroupId;
    @JsonProperty("course_id")
    public int courseId;
    public String name;
    public ArrayList<Rubric> rubric;
    public boolean published;
    @JsonProperty("only_visible_to_overrides")
    public boolean onlyVisibleToOverrides;
    @JsonProperty("visible_to_everyone")
    public boolean visibleToEveryone;
    @JsonProperty("locked_for_user")
    public boolean lockedForUser;
    @JsonProperty("submissions_download_url")
    public String submissionsDownloadUrl;
}