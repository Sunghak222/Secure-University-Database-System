package hk.polyu.comp.project3335.securedb.controller;

import hk.polyu.comp.project3335.securedb.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
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
    @GetMapping("/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getOneById(@PathVariable Long studentId, HttpServletRequest request) {
        Long tokenStudentId = jwtUtil.extractStudentId(request);

        if (!isOwner(request, studentId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }

        return studentService.getOneById(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update student profile with PATCH
    @PatchMapping("/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> updateOneById(@PathVariable Long studentId, @RequestBody UpdateStudentDto updateDto,
                                           HttpServletRequest request) {
        if (!isOwner(request, studentId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }

        return studentService.updateOneById(studentId, updateDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{studentId}/grades")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getGrades(@PathVariable Long studentId, HttpServletRequest request) {

        if (!isOwner(request, studentId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }

        return ResponseEntity.ok(gradeService.listByStudent(studentId));
    }

    @GetMapping("/{studentId}/disciplinary-records")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getDisciplinaryRecords(@PathVariable Long studentId, HttpServletRequest request) {

        if (!isOwner(request, studentId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }

        return ResponseEntity.ok(disciplinaryRecordService.listByStudent(studentId));
    }
}
