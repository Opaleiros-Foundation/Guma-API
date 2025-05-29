package university.jala.gumaapi.mock;

import university.jala.gumaapi.dtos.response.canvas.Assignment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AssignmentMock {
    public static Assignment createAValidAssignment() {
        return Assignment.builder()
                .assignmentGroupId(1432)
                .description("this is the description")
                .dueAt(new Date())
                .courseId(122)
                .gradingType("grading")
                .name("Calculus 2")
                .lockAt(new Date())
                .lockedForUser(false)
                .rubric(new ArrayList<>(List.of(RubricMock.createAValidRubricMock())))
                .build();
    }
}
