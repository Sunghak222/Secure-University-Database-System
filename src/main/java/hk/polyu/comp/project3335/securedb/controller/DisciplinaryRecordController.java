package hk.polyu.comp.project3335.securedb.controller;

import hk.polyu.comp.project3335.securedb.model.DisciplinaryRecord;
import hk.polyu.comp.project3335.securedb.model.Student;
import hk.polyu.comp.project3335.securedb.service.DisciplinaryRecordService;
import hk.polyu.comp.project3335.securedb.service.StudentService;
import hk.polyu.comp.project3335.securedb.security.JwtUtil;
import hk.polyu.comp.project3335.securedb.Dto.DisciplinaryRecordDto.CreateDisciplinaryRecordDto;
import hk.polyu.comp.project3335.securedb.Dto.DisciplinaryRecordDto.UpdateDisciplinaryRecordDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/disciplinary-records")
public class DisciplinaryRecordController {

    private final DisciplinaryRecordService disciplinaryRecordService;
    private final StudentService studentService;
    private final JwtUtil jwtUtil;

    public DisciplinaryRecordController(
            DisciplinaryRecordService disciplinaryRecordService,
            StudentService studentService,
            JwtUtil jwtUtil) {
        this.disciplinaryRecordService = disciplinaryRecordService;
        this.studentService = studentService;
        this.jwtUtil = jwtUtil;
    }

    // GET /disciplinary-records/{studentId} - Students, Guardians, and DRO can view
    @GetMapping("/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('GUARDIAN') or hasRole('DRO')")
    public ResponseEntity<?> getRecordsByStudentId(
            @PathVariable Long studentId,
            HttpServletRequest request) {

        // Retrieve current user information from JWT
        String token = jwtUtil.resolveToken(request);
        String role = jwtUtil.getRole(token);

        // Perform different permission checks based on the role
        if ("STUDENT".equals(role)) {
            // Students can only view their own disciplinary records
            Long jwtStudentId = jwtUtil.extractStudentId(request);
            if (jwtStudentId == null || !jwtStudentId.equals(studentId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Access denied. You can only view your own disciplinary records.");
            }
        } else if ("GUARDIAN".equals(role)) {
            // Guardians can only view the disciplinary records of the students they are guardians for
            Long guardianId = jwtUtil.extractGuardianId(request);

            // Check if the student belongs to this guardian
            Optional<Student> studentOpt = studentService.getOneById(studentId);
            if (studentOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Student student = studentOpt.get();
            if (guardianId == null || !guardianId.equals(student.getGuardianId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Access denied. You can only view your child's disciplinary records.");
            }
        }
        // The DRO role can view all students' disciplinary records without requiring additional checks

        List<DisciplinaryRecord> records = disciplinaryRecordService.listByStudent(studentId);
        return ResponseEntity.ok(records);
    }

    // POST /disciplinary-records - DRO can add new disciplinary records
    @PostMapping
    @PreAuthorize("hasRole('DRO')")
    public ResponseEntity<?> addDisciplinaryRecord(@RequestBody CreateDisciplinaryRecordDto createDto) {
        try {
            DisciplinaryRecord record = disciplinaryRecordService.create(
                createDto.getStudentId(),
                createDto.getDate(),
                createDto.getStaffId(),
                createDto.getDescription()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(record);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /disciplinary-records/all - DRO can view all disciplinary records
    @GetMapping("/all")
    @PreAuthorize("hasRole('DRO')")
    public ResponseEntity<List<DisciplinaryRecord>> getAllRecords() {
        List<DisciplinaryRecord> records = disciplinaryRecordService.listAll();
        return ResponseEntity.ok(records);
    }

    // PUT /disciplinary-records/{id} - DRO can update disciplinary records
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DRO')")
    public ResponseEntity<?> updateDisciplinaryRecord(
            @PathVariable Long id,
            @RequestBody UpdateDisciplinaryRecordDto updateDto) {
        return disciplinaryRecordService.update(id, updateDto.getDate(), updateDto.getDescription())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /disciplinary-records/{id} - DRO can delete disciplinary records
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DRO')")
    public ResponseEntity<?> deleteDisciplinaryRecord(@PathVariable Long id) {
        boolean deleted = disciplinaryRecordService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
