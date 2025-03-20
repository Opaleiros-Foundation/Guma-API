package university.jala.gumaapi.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import university.jala.gumaapi.dtos.response.canvas.Assignment;
import university.jala.gumaapi.dtos.response.canvas.Course;

import java.util.List;

@FeignClient(name = "${canvas.feign.name}", url = "${canvas.feign.url}")
public interface CanvasService {

    @GetMapping("/courses/{courseId}/assignments?access_token={accessToken}&enrollment_state=active")
    List<Assignment> getAssignmentsByCourseId(@PathVariable int courseId, @PathVariable String accessToken);

    @GetMapping("/courses/{courseId}/assignments/{assignmentId}?access_token={accessToken}&enrollment_state=active")
    Assignment getAssignmentById(@PathVariable int courseId, @PathVariable int assignmentId, @PathVariable String accessToken);

    @GetMapping("/courses?access_token={accessToken}&enrollment_state=active")
    List<Course> getAllCourse(@PathVariable String accessToken);
}
