package hk.polyu.comp.project3335.securedb.service;

import hk.polyu.comp.project3335.securedb.model.Grade;
import hk.polyu.comp.project3335.securedb.repository.GradeRepository;
import hk.polyu.comp.project3335.securedb.repository.StudentRepository;
import hk.polyu.comp.project3335.securedb.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public GradeService(GradeRepository gradeRepository, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public Grade create(Long studentId, Long courseId, String term, String grade, String comments) {
        // Validate that student exists
        if (!studentRepository.existsById(studentId)) {
            throw new IllegalArgumentException("Student with ID " + studentId + " does not exist");
        }
        
        // Validate that course exists
        if (!courseRepository.existsById(courseId)) {
            throw new IllegalArgumentException("Course with ID " + courseId + " does not exist");
        }
        
        Grade g = new Grade(studentId, courseId, term, grade, comments);
        return gradeRepository.save(g);
    }
    public Grade update(Long id,String term, String grade, String comments) {
        Grade g = gradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("grade not found"));
        g.setTerm(term);
        g.setGrade(grade);
        g.setComments(comments);
        return gradeRepository.save(g);
    } 

    public void delete(Long id) {
        if (!gradeRepository.existsById(id)) {
            throw new IllegalArgumentException("Grade not found");
        }
        gradeRepository.deleteById(id);
    }
    public List<Grade> listByStudent(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }
    public List<Grade> listByStudentAndTerm(Long studentId, String term) {
        return gradeRepository.findByStudentIdAndTerm(studentId, term);
    }

    public List<Grade> listAll() {
        return gradeRepository.findAll();
    }

}
