package hk.polyu.comp.project3335.securedb.repository;

import hk.polyu.comp.project3335.securedb.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByGuardianId(Long guardianId);
    Optional<Student> findByEmail(String email);
    Optional<Student> findById(Long id);
}
