package hk.polyu.comp.project3335.securedb.controller;

import hk.polyu.comp.project3335.securedb.Dto.GuardianDecryptedDto;
import hk.polyu.comp.project3335.securedb.Dto.StudentDecryptedDto;
import hk.polyu.comp.project3335.securedb.model.DisciplinaryRecord;
import hk.polyu.comp.project3335.securedb.model.Grade;
import hk.polyu.comp.project3335.securedb.model.Student;
import hk.polyu.comp.project3335.securedb.security.JwtUtil;
import hk.polyu.comp.project3335.securedb.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import hk.polyu.comp.project3335.securedb.model.Guardian;
import hk.polyu.comp.project3335.securedb.service.GuardianService;
import hk.polyu.comp.project3335.securedb.service.GradeService;
import hk.polyu.comp.project3335.securedb.service.DisciplinaryRecordService;
import hk.polyu.comp.project3335.securedb.Dto.GuardianDto.UpdateGuardianDto;

import java.util.List;

@RestController
@RequestMapping("/api/guardians")
public class GuardianController {

    private final GuardianService guardianService;
    private final StudentService studentService;
    private final GradeService gradeService;
    private final DisciplinaryRecordService disciplinaryRecordService;
    private final JwtUtil jwtUtil;

    public GuardianController(GuardianService guardianService, StudentService studentService,
                              GradeService gradeService, DisciplinaryRecordService disciplinaryRecordService,
                              JwtUtil jwtUtil) {
        this.guardianService = guardianService;
        this.studentService = studentService;
        this.gradeService = gradeService;
        this.disciplinaryRecordService = disciplinaryRecordService;
        this.jwtUtil = jwtUtil;
    }

    //get guardians info
    @GetMapping("/me")
    @PreAuthorize("hasRole('GUARDIAN')")
    public ResponseEntity<GuardianDecryptedDto> getGuardianInfo(HttpServletRequest request) {
        Long guardianId = jwtUtil.extractGuardianId(request);
        if (guardianId == null) return ResponseEntity.status(403).build();

        return guardianService.getOneById(guardianId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/me")
    @PreAuthorize("hasRole('GUARDIAN')")
    public ResponseEntity<GuardianDecryptedDto> updateGuardianInfo(HttpServletRequest request,
                                                                    @RequestBody UpdateGuardianDto updateDto) {
        Long guardianId = jwtUtil.extractGuardianId(request);
        if (guardianId == null) return ResponseEntity.status(403).build();

        Guardian g = new Guardian();
        g.setFirstName(updateDto.getFirstName());
        g.setLastName(updateDto.getLastName());
        g.setEmail(updateDto.getEmail());
        g.setPhone(updateDto.getPhone());

        return guardianService.updateOneById(guardianId, g)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get students under this guardian
    @GetMapping("/me/students")
    @PreAuthorize("hasRole('GUARDIAN')")
    public ResponseEntity<List<StudentDecryptedDto>> getMyStudents(HttpServletRequest request) {
        Long guardianId = jwtUtil.extractGuardianId(request);
        if (guardianId == null) return ResponseEntity.status(403).build();

        List<StudentDecryptedDto> students =
                studentService.getStudentsByGuardianId(guardianId);

        return ResponseEntity.ok(students);
    }

}
