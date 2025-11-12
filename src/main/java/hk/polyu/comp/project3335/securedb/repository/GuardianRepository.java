package hk.polyu.comp.project3335.securedb.repository;

import hk.polyu.comp.project3335.securedb.model.Guardian;
import hk.polyu.comp.project3335.securedb.model.Student;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuardianRepository extends JpaRepository<Guardian, Long> {

    Optional<Guardian> findByEmail(String email);
    Optional<Guardian> findById(String id);
}
