package hk.polyu.comp.project3335.securedb.service;

import hk.polyu.comp.project3335.securedb.Dto.StudentDecryptedDto;
import hk.polyu.comp.project3335.securedb.Dto.StudentDto.UpdateStudentDto;
import hk.polyu.comp.project3335.securedb.model.Student;
import hk.polyu.comp.project3335.securedb.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Value("${app.crypto.key}")
    private String cryptoKey;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Optional<StudentDecryptedDto> findByEmail(String email) {
        return studentRepository.findDecryptedByEmail(email, cryptoKey)
                .map(StudentDecryptedDto::from);
    }

    public Optional<StudentDecryptedDto> getOneById(Long id) {
        return studentRepository.findDecryptedById(id, cryptoKey)
                .map(StudentDecryptedDto::from);
    }

    //update unencrypted information
    public Optional<StudentDecryptedDto> updateOneById(Long id, UpdateStudentDto updated) {
        return studentRepository.findById(id).map(student -> {
            if (updated.getFirstName() != null) student.setFirstName(updated.getFirstName());
            if (updated.getLastName() != null) student.setLastName(updated.getLastName());
            if (updated.getGender() != null) student.setGender(updated.getGender());
            if (updated.getEmail() != null) student.setEmail(updated.getEmail());
            if (updated.getEnrollmentYear() != null) student.setEnrollmentYear(updated.getEnrollmentYear());
            if (updated.getGuardianRelation() != null) student.setGuardianRelation(updated.getGuardianRelation());

            studentRepository.save(student);

            return getOneById(id).orElse(null);
        });
    }

    public boolean isChildOfGuardian(Long studentId, Long guardianId) {
        return studentRepository.findById(studentId)
                .map(s -> s.getGuardianId() != null && s.getGuardianId().equals(guardianId))
                .orElse(false);
    }

    public List<StudentDecryptedDto> getStudentsByGuardianId(Long guardianId) {
        return studentRepository.findDecryptedByGuardianId(guardianId, cryptoKey)
                .stream()
                .map(StudentDecryptedDto::from)
                .toList();
    }
    public Optional<StudentDecryptedDto> update(Long id, UpdateStudentDto dto) {
        studentRepository.updateEncrypted(
                id,
                dto.getLastName(),
                dto.getFirstName(),
                dto.getGender(),
                dto.getIdentificationNumber(),
                dto.getAddress(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getEnrollmentYear(),
                dto.getGuardianRelation(),
                cryptoKey
        );

        return studentRepository.findDecryptedById(id, cryptoKey)
                .map(StudentDecryptedDto::from);
    }
}
