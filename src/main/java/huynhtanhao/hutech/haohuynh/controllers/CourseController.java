package huynhtanhao.hutech.haohuynh.controllers;

import huynhtanhao.hutech.haohuynh.models.Course;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @GetMapping
    public String index(Model model) {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("SWT301", "Kỹ thuật phần mềm", "https://files.fullstack.edu.vn/f8-prod/courses/1.png"));
        courses.add(new Course("INT301", "Công nghệ Web Java", "https://files.fullstack.edu.vn/f8-prod/courses/7.png"));
        courses.add(new Course("ITE302", "Kiểm thử phần mềm", "https://files.fullstack.edu.vn/f8-prod/courses/12.png"));
        courses.add(new Course("PRj301", "Lập trình Java nâng cao",
                "https://files.fullstack.edu.vn/f8-prod/courses/2.png"));

        model.addAttribute("courses", courses);
        return "course/list";
    }
}
