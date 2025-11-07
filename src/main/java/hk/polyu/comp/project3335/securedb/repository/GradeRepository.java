package hk.polyu.comp.project3335.securedb.repository;

import hk.polyu.comp.project3335.securedb.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<Grade> findById(long id);
    List<Grade> findByStudentId(long studentId);
    List<Grade> findByStudentIdAndTerm(long studentId, String term);
    Optional<Grade> findByStudentIdAndCourseId(Long studentId, Long courseId);
}
