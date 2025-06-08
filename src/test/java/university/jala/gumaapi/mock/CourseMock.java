package university.jala.gumaapi.mock;

import university.jala.gumaapi.dtos.response.canvas.Course;

import java.util.List;

public class CourseMock {
    public static List<Course> getCourses() {
        Course ds = Course.builder()
                .id(1)
                .courseCode("324")
                .defaultView("fdj;sakl")
                .name("DS")
                .build();

        return List.of(ds);
    }
}
