package hk.polyu.comp.project3335.securedb.service;

import hk.polyu.comp.project3335.securedb.Dto.GuardianDecryptedDto;
import hk.polyu.comp.project3335.securedb.model.Guardian;
import hk.polyu.comp.project3335.securedb.model.Student;
import hk.polyu.comp.project3335.securedb.repository.GuardianRepository;
import hk.polyu.comp.project3335.securedb.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GuardianService {

    private final GuardianRepository guardianRepository;
    private static final Logger logger = LoggerFactory.getLogger(GuardianService.class);

    @Value("${app.crypto.key}")
    private String cryptoKey;

    public GuardianService(GuardianRepository guardianRepository) {
        this.guardianRepository = guardianRepository;
    }

    public Optional<GuardianDecryptedDto> findById(Long id) {
        return guardianRepository.findDecryptedById(id, cryptoKey)
                .map(GuardianDecryptedDto::from);
    }

    public Optional<GuardianDecryptedDto> findByEmail(String email) {
        return guardianRepository.findDecryptedByEmail(email, cryptoKey)
                .map(GuardianDecryptedDto::from);
    }

    public Optional<GuardianDecryptedDto> getOneById(Long id) {
        logger.debug("Fetching guardian by ID {}", id);
        return guardianRepository.findDecryptedById(id, cryptoKey)
                .map(GuardianDecryptedDto::from);
    }

    @Transactional
    public Optional<GuardianDecryptedDto> updateOneById(Long id, Guardian updatedGuardian) {
        logger.debug("Updating guardian by ID {}", id);
        return guardianRepository.findById(id).map(existing -> {

            if (updatedGuardian.getFirstName() != null) {
                existing.setFirstName(updatedGuardian.getFirstName());
            }
            if (updatedGuardian.getLastName() != null) {
                existing.setLastName(updatedGuardian.getLastName());
            }
            if (updatedGuardian.getEmail() != null) {
                existing.setEmail(updatedGuardian.getEmail());
            }

            if (updatedGuardian.getPhone() != null) {
                guardianRepository.updateEncryptedPhone(
                        id,
                        updatedGuardian.getPhone(),
                        cryptoKey
                );
            }

            guardianRepository.save(existing);

            return getOneById(id).orElse(null);
        });
    }
}
