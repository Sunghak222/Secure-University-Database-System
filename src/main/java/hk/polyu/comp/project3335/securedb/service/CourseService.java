package hk.polyu.comp.project3335.securedb.service;

import hk.polyu.comp.project3335.securedb.model.Course;
import hk.polyu.comp.project3335.securedb.repository.CourseRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course create(Course course) {
        return courseRepository.save(course);
    }

    public Course update(Long id, String code, String name) {
        Course c = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("course not found"));

        if (name != null) c.setCourseName(name.trim());
        if (code != null) c.setCode(code.trim());

        return courseRepository.save(c);
    }

    public Course getByCode(String code) {
        return courseRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("course not found"));
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
    }
}
