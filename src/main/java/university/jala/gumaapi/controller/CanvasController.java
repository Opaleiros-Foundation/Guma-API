package university.jala.gumaapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import university.jala.gumaapi.dtos.response.canvas.Assignment;
import university.jala.gumaapi.dtos.response.canvas.Course;
import university.jala.gumaapi.service.CanvasService;
import university.jala.gumaapi.swagger.annotations.OkResponse;

import java.util.List;

@RestController
@RequestMapping("/canvas")
@RequiredArgsConstructor
@Tag(name = "Canvas", description = "Controller responsible for calling canvas")
public class CanvasController {
    private final CanvasService canvasService;

    @Operation(summary = "Get all courses enrolled", description = "get all courses which student is enrolled")
    @GetMapping("/courses")
    @OkResponse
    public ResponseEntity<List<Course>> getAllCoursesEnrolled(@RequestHeader(value = "access_token") String token) {
        List<Course> allCourse = this.canvasService.getAllCourse(token);
        return ResponseEntity.ok(allCourse);
    }

    @Operation(summary = "Get all Assignment by course", description = "Get all assignments available in the course")
    @GetMapping("/assignments/{courseId}")
    @OkResponse
    public ResponseEntity<List<Assignment>> getAssignmentById(@PathVariable int courseId, @RequestHeader(value = "access_token") String token) {
        List<Assignment> AllAssignmentsFromCourse = this.canvasService.getAssignmentsByCourseId(courseId, token);
        return ResponseEntity.ok(AllAssignmentsFromCourse);
    }

    @Operation(summary = "Get the assignment by id", description = "get assignment by id")
    @GetMapping("/assignments/{courseId}/{assignmentId}")
    @OkResponse
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable int courseId, @PathVariable int assignmentId, @RequestHeader(value = "access_token") String token) {
        Assignment assignmentFromCourse = this.canvasService.getAssignmentById(courseId, assignmentId, token);
        return ResponseEntity.ok(assignmentFromCourse);
    }
}
