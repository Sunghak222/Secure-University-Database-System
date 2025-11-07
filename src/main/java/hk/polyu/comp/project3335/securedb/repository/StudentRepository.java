package hk.polyu.comp.project3335.securedb.repository;

import hk.polyu.comp.project3335.securedb.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findByEmail(String email);
}
