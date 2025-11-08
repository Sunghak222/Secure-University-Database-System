package hk.polyu.comp.project3335.securedb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import hk.polyu.comp.project3335.securedb.model.Guardian;
import hk.polyu.comp.project3335.securedb.service.GuardianService;
import hk.polyu.comp.project3335.securedb.service.GradeService;
import hk.polyu.comp.project3335.securedb.service.DisciplinaryRecordService;
import hk.polyu.comp.project3335.securedb.Dto.GuardianDto.UpdateGuardianDto;

@RestController
@RequestMapping("/guardians")
public class GuardianController {
    
    private final GuardianService guardianService;

    public GuardianController(GuardianService guardianService, 
                             GradeService gradeService,
                             DisciplinaryRecordService disciplinaryRecordService) {
        this.guardianService = guardianService;
    }

     // Guardian maintains personal information
    @GetMapping("/{guardianId}")
    @PreAuthorize("hasRole('GUARDIAN')")
    public ResponseEntity<Guardian> getOneById(@PathVariable String guardianId) {
        Guardian guardian = guardianService.getOneById(guardianId).orElse(null);
        if (guardian == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(guardian);
    }

    // Update guardian profile with PATCH
    @PatchMapping("/{guardianId}")
    @PreAuthorize("hasRole('GUARDIAN')")
    public ResponseEntity<Guardian> updateOneById(@PathVariable String guardianId, 
                                                 @RequestBody UpdateGuardianDto updateDto) {
        // Convert DTO to Guardian entity
        Guardian updatedGuardian = new Guardian();
        updatedGuardian.setFirstName(updateDto.getFirstName());
        updatedGuardian.setLastName(updateDto.getLastName());
        updatedGuardian.setEmail(updateDto.getEmail());
        updatedGuardian.setPhone(updateDto.getPhone());

        // Use service method for partial update
        return guardianService.updateOneById(guardianId, updatedGuardian)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
