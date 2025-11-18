package hk.polyu.comp.project3335.securedb.controller;

import hk.polyu.comp.project3335.securedb.Dto.StudentDecryptedDto;
import hk.polyu.comp.project3335.securedb.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import hk.polyu.comp.project3335.securedb.model.Student;
import hk.polyu.comp.project3335.securedb.Dto.StudentDto.UpdateStudentDto;
import hk.polyu.comp.project3335.securedb.service.StudentService;
import hk.polyu.comp.project3335.securedb.service.GradeService;
import hk.polyu.comp.project3335.securedb.service.DisciplinaryRecordService;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final GradeService gradeService;
    private final DisciplinaryRecordService disciplinaryRecordService;
    private final JwtUtil jwtUtil;

    public StudentController(
            StudentService studentService,
            GradeService gradeService,
            DisciplinaryRecordService disciplinaryRecordService,
            JwtUtil jwtUtil
    ) {
        this.studentService = studentService;
        this.gradeService = gradeService;
        this.disciplinaryRecordService = disciplinaryRecordService;
        this.jwtUtil = jwtUtil;
    }

    private boolean isOwner(HttpServletRequest request, Long pathStudentId) {
        Long jwtStudentId = jwtUtil.extractStudentId(request);
        return jwtStudentId != null && jwtStudentId.equals(pathStudentId);
    }

    // Student maintains personal information
    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentDecryptedDto> getStudentInfo(HttpServletRequest request) {
        Long studentId = jwtUtil.extractStudentId(request);
        if (studentId == null) return ResponseEntity.status(403).build();

        return studentService.getOneById(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update student profile with PATCH
    @PatchMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentDecryptedDto> updateStudentInfo(@RequestBody UpdateStudentDto updateDto,
                                           HttpServletRequest request) {
        Long studentId = jwtUtil.extractStudentId(request);
        if (studentId == null) return ResponseEntity.status(403).build();

        return studentService.update(studentId, updateDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
