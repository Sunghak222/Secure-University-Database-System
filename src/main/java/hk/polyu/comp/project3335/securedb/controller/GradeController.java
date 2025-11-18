package hk.polyu.comp.project3335.securedb.controller;

import hk.polyu.comp.project3335.securedb.Dto.GradeDecryptedDto;
import hk.polyu.comp.project3335.securedb.security.JwtUtil;
import hk.polyu.comp.project3335.securedb.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import hk.polyu.comp.project3335.securedb.Dto.GradeDto.CreateGradeDto;
import hk.polyu.comp.project3335.securedb.Dto.GradeDto.UpdateGradeDto;
import hk.polyu.comp.project3335.securedb.model.Grade;
import hk.polyu.comp.project3335.securedb.model.Student;
import hk.polyu.comp.project3335.securedb.service.GradeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;
    private final StudentService studentService;
    private final JwtUtil jwtUtil;

    public GradeController(GradeService gradeService, StudentService studentService, JwtUtil jwtUtil) {
        this.gradeService = gradeService;
        this.studentService = studentService;
        this.jwtUtil = jwtUtil;
    }

    // Student accesses their grades
    @GetMapping("/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('GUARDIAN') or hasRole('ARO')")
    public ResponseEntity<?> getGradesByStudentId(
            @PathVariable Long studentId,
            HttpServletRequest request) {

        // Retrieve current user information from JWT
        String token = jwtUtil.resolveToken(request);
        String role = jwtUtil.getRole(token);

        // Perform different permission checks based on the role.
        if ("STUDENT".equals(role)) {
            // Students can only view their own grades.
            Long jwtStudentId = jwtUtil.extractStudentId(request);
            if (jwtStudentId == null || !jwtStudentId.equals(studentId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Access denied. You can only view your own grades.");
            }
        } else if ("GUARDIAN".equals(role)) {
            // Guardians can only view the grades of the students they are guardians for.
            Long guardianId = jwtUtil.extractGuardianId(request);
            var studentOpt = studentService.getOneById(studentId);

            if (studentOpt.isEmpty()) return ResponseEntity.notFound().build();

            if (guardianId == null || !guardianId.equals(studentOpt.get().getGuardianId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Access denied. You can only view your child's grades.");
            }
        }
        // The ARO role can view all students' grades without requiring additional checks.

        return ResponseEntity.ok(gradeService.listByStudent(studentId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ARO')")
    public ResponseEntity<?> addGrade(@RequestBody CreateGradeDto createGradeDto) {
        try {
            gradeService.create(
                    createGradeDto.getStudentId(),
                    createGradeDto.getCourseId(),
                    createGradeDto.getTerm(),
                    createGradeDto.getGrade(),
                    createGradeDto.getComments()
            );

            // Created grade again decrypting for response
            var result = gradeService.listByStudentAndCourse(
                    createGradeDto.getStudentId(),
                    createGradeDto.getCourseId()
            );

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ARO')")
    public ResponseEntity<List<GradeDecryptedDto>> getAllGrades() {
        return ResponseEntity.ok(gradeService.listAll());
    }

    @PutMapping("/{gradeId}")
    @PreAuthorize("hasRole('ARO')")
    public ResponseEntity<?> updateGrade(
            @PathVariable Long gradeId,
            @RequestBody UpdateGradeDto updateGradeDto) {
        try {
            gradeService.update(
                    gradeId,
                    updateGradeDto.getTerm(),
                    updateGradeDto.getGrade(),
                    updateGradeDto.getComments()
            );

            // Respond with updated decrypted DTO
            return ResponseEntity.ok(
                    gradeService.getById(gradeId)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{gradeId}")
    @PreAuthorize("hasRole('ARO')")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long gradeId) {
        try {
            gradeService.delete(gradeId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
