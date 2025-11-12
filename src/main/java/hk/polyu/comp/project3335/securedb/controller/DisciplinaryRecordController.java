package hk.polyu.comp.project3335.securedb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import hk.polyu.comp.project3335.securedb.model.DisciplinaryRecord;
import hk.polyu.comp.project3335.securedb.service.DisciplinaryRecordService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/disciplinary-records")
public class DisciplinaryRecordController {
    
    private final DisciplinaryRecordService disciplinaryRecordService;

    public DisciplinaryRecordController(DisciplinaryRecordService disciplinaryRecordService) {
        this.disciplinaryRecordService = disciplinaryRecordService;
    }

    // Student accesses their disciplinary records
    @GetMapping("/disciplinary-records")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<DisciplinaryRecord>> getMyDisciplinaryRecords(@RequestParam Long studentId) {
        List<DisciplinaryRecord> records = disciplinaryRecordService.listByStudent(studentId);
        return ResponseEntity.ok(records);
    }

    /*// DRO: Add a new disciplinary record
    @PostMapping
    @PreAuthorize("hasRole('DRO')")
    public ResponseEntity<DisciplinaryRecord> addRecord(@RequestBody DisciplinaryRecordRequest request) {
        DisciplinaryRecord record = disciplinaryRecordService.create(
            request.getStudentId(),
            request.getDate(),
            request.getStaffId(),
            request.getDescriptions()
        );
        return ResponseEntity.ok(record);
    }

    // DRO: Query all disciplinary records
    @GetMapping
    @PreAuthorize("hasRole('DRO')")
    public ResponseEntity<List<DisciplinaryRecord>> getAllRecords() {
        List<DisciplinaryRecord> records = disciplinaryRecordService.listAll();
        return ResponseEntity.ok(records);
    }

    // DRO: Query disciplinary records by student ID
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('DRO')")
    public ResponseEntity<List<DisciplinaryRecord>> getRecordsByStudent(@PathVariable Long studentId) {
        List<DisciplinaryRecord> records = disciplinaryRecordService.listByStudent(studentId);
        return ResponseEntity.ok(records);
    }

    // DRO: Query a specific disciplinary record by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DRO')")
    public ResponseEntity<DisciplinaryRecord> getRecordById(@PathVariable Long id) {
        DisciplinaryRecord record = disciplinaryRecordService.getById(id).orElse(null);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(record);
    }

    // DRO: Modify a disciplinary record
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DRO')")
    public ResponseEntity<DisciplinaryRecord> updateRecord(
            @PathVariable Long id,
            @RequestBody DisciplinaryRecordUpdateRequest request) {
        try {
            DisciplinaryRecord updatedRecord = disciplinaryRecordService.update(
                id, 
                request.getDate(), 
                request.getDescriptions()
            );
            return ResponseEntity.ok(updatedRecord);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DRO: Delete a disciplinary record
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DRO')")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        disciplinaryRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Inner classes for request bodies
    public static class DisciplinaryRecordRequest {
        private Long studentId;
        private LocalDate date;
        private Long staffId;
        private String descriptions;

        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        public Long getStaffId() { return staffId; }
        public void setStaffId(Long staffId) { this.staffId = staffId; }
        public String getDescriptions() { return descriptions; }
        public void setDescriptions(String descriptions) { this.descriptions = descriptions; }
    }

    public static class DisciplinaryRecordUpdateRequest {
        private LocalDate date;
        private String descriptions;

        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        public String getDescriptions() { return descriptions; }
        public void setDescriptions(String descriptions) { this.descriptions = descriptions; }
    }*/
}
