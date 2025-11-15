package hk.polyu.comp.project3335.securedb.service;

import hk.polyu.comp.project3335.securedb.Dto.StudentDto.UpdateStudentDto;
import hk.polyu.comp.project3335.securedb.model.Student;
import hk.polyu.comp.project3335.securedb.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student save(Student s) {
        return studentRepository.save(s);
    }

    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public Optional<Student> getOneById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> updateOneById(Long id, UpdateStudentDto updatedStudent) {
        return studentRepository.findById(id).map(existingStudent -> {
            // Only update fields that are not null (partial update)
            if (updatedStudent.getFirstName() != null) {
                existingStudent.setFirstName(updatedStudent.getFirstName());
            }
            if (updatedStudent.getLastName() != null) {
                existingStudent.setLastName(updatedStudent.getLastName());
            }
            if (updatedStudent.getGender() != null) {
                existingStudent.setGender(updatedStudent.getGender());
            }
            if (updatedStudent.getIdentificationNumber() != null) {
                existingStudent.setIdentificationNumber(updatedStudent.getIdentificationNumber());
            }
            if (updatedStudent.getAddress() != null) {
                existingStudent.setAddress(updatedStudent.getAddress());
            }
            if (updatedStudent.getEmail() != null) {
                existingStudent.setEmail(updatedStudent.getEmail());
            }
            if (updatedStudent.getPhone() != null) {
                existingStudent.setPhone(updatedStudent.getPhone());
            }
            if (updatedStudent.getEnrollmentYear() != null) {
                existingStudent.setEnrollmentYear(updatedStudent.getEnrollmentYear());
            }
            if (updatedStudent.getGuardianRelation() != null) {
                existingStudent.setGuardianRelation(updatedStudent.getGuardianRelation());
            }
            return studentRepository.save(existingStudent);
        });
    }
    public boolean isChildOfGuardian(Long studentId, Long guardianId) {

        return studentRepository.findById(studentId)
                .map(s -> s.getGuardianId() != null && s.getGuardianId().equals(guardianId))
                .orElse(false);
    }
    public List<Student> getStudentsByGuardianId(Long guardianId) {
        return studentRepository.findByGuardianId(guardianId);
    }
}
