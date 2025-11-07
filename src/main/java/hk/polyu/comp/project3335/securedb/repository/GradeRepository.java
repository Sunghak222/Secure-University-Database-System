package hk.polyu.comp.project3335.securedb.repository;

import hk.polyu.comp.project3335.securedb.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    Grade findByStudentIdAndCourseId(Long studentId, Long courseId);
}
