package hk.polyu.comp.project3335.securedb.service;

import hk.polyu.comp.project3335.securedb.model.Grade;
import hk.polyu.comp.project3335.securedb.repository.GradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public Grade create(Long studentId, Long courseId, String term, String grade, String comments) {
        Grade g = new Grade(studentId, courseId, term, grade, comments);
        return gradeRepository.save(g);
    }
    public Grade update(Long id, String grade, String comments) {
        Grade g = gradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("grade not found"));
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
