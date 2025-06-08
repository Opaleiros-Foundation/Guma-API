package university.jala.gumaapi.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import university.jala.gumaapi.dtos.response.canvas.Assignment;
import university.jala.gumaapi.dtos.response.canvas.Course;
import university.jala.gumaapi.mock.AssignmentMock;
import university.jala.gumaapi.mock.CourseMock;
import university.jala.gumaapi.service.CanvasService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class CanvasControllerTest {

    @InjectMocks
    CanvasController canvasController;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    CanvasService canvasService;

    @BeforeEach
    void setUp() {
        when(canvasService.getAssignmentById(anyInt(), anyInt(), any()))
                .thenReturn(AssignmentMock.createAValidAssignment());
        when(canvasService.getAllCourse(anyString())).thenReturn(CourseMock.getCourses());
        when(canvasService.getAssignmentsByCourseId(anyInt(), anyString()))
                .thenReturn(List.of(AssignmentMock.createAValidAssignment()));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should get all courses successfully")
    public void getAllCourses() {

        List<Course> response = this.canvasService.getAllCourse("token");
        assertNotNull(response);
        assertFalse(response.isEmpty());
    }

    @Test
    @DisplayName("Should get assignment by id")
    public void getAssignmentById() {
        Assignment response = this.canvasService.getAssignmentById(1, 1, "token");

        assertNotNull(response);
        assertEquals(AssignmentMock.createAValidAssignment().getCourseId(), response.getCourseId());
    }

    @Test
    @DisplayName("Should get assignment by course id")
    public void getAssignmentsByCourseId() {
        List<Assignment> response = this.canvasService.getAssignmentsByCourseId(1, "token");

        assertNotNull(response);
        assertFalse(response.isEmpty());
    }
}