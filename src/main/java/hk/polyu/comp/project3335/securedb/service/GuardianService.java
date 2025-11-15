package hk.polyu.comp.project3335.securedb.service;

import hk.polyu.comp.project3335.securedb.model.Guardian;
import hk.polyu.comp.project3335.securedb.model.Student;
import hk.polyu.comp.project3335.securedb.repository.GuardianRepository;
import hk.polyu.comp.project3335.securedb.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GuardianService {

    private final GuardianRepository guardianRepository;

    public GuardianService(GuardianRepository guardianRepository) {
        this.guardianRepository = guardianRepository;
    }

    public Guardian save(Guardian guardian) {
        return guardianRepository.save(guardian);
    }
    public Optional<Guardian> findByEmail(String email) {
        return guardianRepository.findByEmail(email);
    }

    public Optional<Guardian> getOneById(Long id) {
        return guardianRepository.findById(id);
    }

    public Optional<Guardian> updateOneById(Long id, Guardian updatedGuardian) {
        return guardianRepository.findById(id).map(existingGuardian -> {
            // Only update fields that are not null (partial update)
            if (updatedGuardian.getFirstName() != null) {
                existingGuardian.setFirstName(updatedGuardian.getFirstName());
            }
            if (updatedGuardian.getLastName() != null) {
                existingGuardian.setLastName(updatedGuardian.getLastName());
            }
            if (updatedGuardian.getEmail() != null) {
                existingGuardian.setEmail(updatedGuardian.getEmail());
            }
            if (updatedGuardian.getPhone() != null) {
                existingGuardian.setPhone(updatedGuardian.getPhone());
            }
            return guardianRepository.save(existingGuardian);
        });
    }
}
