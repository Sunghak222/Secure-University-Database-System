package hk.polyu.comp.project3335.securedb.controller;

import hk.polyu.comp.project3335.securedb.Dto.DisciplinaryRecordDecryptedDto;
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

    @GetMapping("/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('GUARDIAN') or hasRole('DRO')")
    public ResponseEntity<?> getRecordsByStudentId(
            @PathVariable Long studentId,
            HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        String role = jwtUtil.getRole(token);

        if ("STUDENT".equals(role)) {
            Long jwtStudentId = jwtUtil.extractStudentId(request);
            if (!studentId.equals(jwtStudentId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Access denied. You can only view your own disciplinary records.");
            }
        }

        // GUARDIAN views his children
        else if ("GUARDIAN".equals(role)) {
            Long guardianId = jwtUtil.extractGuardianId(request);

            var studentOpt = studentService.getOneById(studentId);
            if (studentOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            if (!studentOpt.get().getGuardianId().equals(guardianId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Access denied. You can only view your child's disciplinary records.");
            }
        }

        // DRO
        List<DisciplinaryRecordDecryptedDto> records =
                disciplinaryRecordService.listByStudent(studentId);

        return ResponseEntity.ok(records);
    }

    // POST /disciplinary-records - DRO can add new disciplinary records
    @PostMapping
    @PreAuthorize("hasRole('DRO')")
    public ResponseEntity<?> addDisciplinaryRecord(
            @RequestBody CreateDisciplinaryRecordDto createDto) {

        try {
            DisciplinaryRecordDecryptedDto record =
                    disciplinaryRecordService.create(
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
    public ResponseEntity<List<DisciplinaryRecordDecryptedDto>> getAllRecords() {

        List<DisciplinaryRecordDecryptedDto> records =
                disciplinaryRecordService.listAll();

        return ResponseEntity.ok(records);
    }

    // PUT /disciplinary-records/{id} - DRO can update disciplinary records
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DRO')")
    public ResponseEntity<?> updateDisciplinaryRecord(
            @PathVariable Long id,
            @RequestBody UpdateDisciplinaryRecordDto updateDto) {

        return disciplinaryRecordService.update(
                        id,
                        updateDto.getDate(),
                        updateDto.getDescription()
                )
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
