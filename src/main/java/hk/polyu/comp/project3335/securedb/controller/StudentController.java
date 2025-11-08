package hk.polyu.comp.project3335.securedb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import hk.polyu.comp.project3335.securedb.model.Student;
import hk.polyu.comp.project3335.securedb.Dto.StudentDto.UpdateStudentDto;
import hk.polyu.comp.project3335.securedb.service.StudentService;
import hk.polyu.comp.project3335.securedb.service.GradeService;
import hk.polyu.comp.project3335.securedb.service.DisciplinaryRecordService;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService, 
                            GradeService gradeService, 
                            DisciplinaryRecordService disciplinaryRecordService) {
        this.studentService = studentService;
    }

    // Student maintains personal information
    @GetMapping("/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Student> getOneById(@PathVariable String studentId) {
        Student student = studentService.getOneById(studentId).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    // Update student profile with PATCH
    @PatchMapping("/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Student> updateOneById(@PathVariable String studentId, 
                                                 @RequestBody UpdateStudentDto updateDto) {
        // Convert DTO to Student entity
        Student updatedStudent = new Student();
        updatedStudent.setFirstName(updateDto.getFirstName());
        updatedStudent.setLastName(updateDto.getLastName());
        updatedStudent.setGender(updateDto.getGender());
        updatedStudent.setIdentificationNumber(updateDto.getIdentificationNumber());
        updatedStudent.setAddress(updateDto.getAddress());
        updatedStudent.setEmail(updateDto.getEmail());
        updatedStudent.setPhone(updateDto.getPhone());
        updatedStudent.setEnrollmentYear(updateDto.getEnrollmentYear());
        updatedStudent.setGuardianRelation(updateDto.getGuardianRelation());
        
        // Use service method for partial update
        return studentService.updateOneById(studentId, updatedStudent)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
