package hk.polyu.comp.project3335.securedb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import hk.polyu.comp.project3335.securedb.Dto.GradeDto.CreateGradeDto;
import hk.polyu.comp.project3335.securedb.Dto.GradeDto.UpdateGradeDto;
import hk.polyu.comp.project3335.securedb.model.Grade;
import hk.polyu.comp.project3335.securedb.service.GradeService;

import java.util.List;

@RestController
@RequestMapping("/grades")
public class GradeController {
    
    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    // Student accesses their grades
    @GetMapping("/{studentId}")
    @PreAuthorize("hasRole('STUDENT', 'GUARDIAN', 'ARO')")
    public ResponseEntity<List<Grade>> getGradesByStudentId(@PathVariable Long studentId) {
        List<Grade> grades = gradeService.listByStudent(studentId);
        return ResponseEntity.ok(grades);
    }

    @PostMapping
    @PreAuthorize("hasRole('ARO')")
    public ResponseEntity<Grade> addGrade(@RequestBody CreateGradeDto createGradeDto) {
        Grade grade = gradeService.create(
            createGradeDto.getStudentId(),
            createGradeDto.getCourseId(),
            createGradeDto.getTerm(),
            createGradeDto.getGrade(),
            createGradeDto.getComments()
        );
        return ResponseEntity.ok(grade);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ARO')")
    public ResponseEntity<List<Grade>> getAllGrades() {
        List<Grade> grades = gradeService.listAll();
        return ResponseEntity.ok(grades);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ARO')")
    public ResponseEntity<Grade> updateGrade(
            @PathVariable Long id, 
            @RequestBody UpdateGradeDto updateGradeDto) {
        try {
            Grade updatedGrade = gradeService.update(id, updateGradeDto.getGrade(), updateGradeDto.getComments());
            return ResponseEntity.ok(updatedGrade);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ARO')")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        try {
            gradeService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
