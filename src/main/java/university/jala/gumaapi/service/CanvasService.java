package university.jala.gumaapi.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import university.jala.gumaapi.dtos.response.canvas.Assignment;
import university.jala.gumaapi.dtos.response.canvas.Course;

@FeignClient(name = "${canvas.feign.name}", url = "${canvas.feign.url}")
public interface CanvasService {

    @GetMapping("/courses/{courseId}/assignments?access_token={accessToken}&enrollment_state=active")
    Assignment getAssignmentByCourseId(@PathVariable int courseId, @PathVariable String accessToken);

    @GetMapping("/courses?access_token={accessToken}&enrollment_state=active")
    Course getAllCourse(@PathVariable String accessToken);
}
