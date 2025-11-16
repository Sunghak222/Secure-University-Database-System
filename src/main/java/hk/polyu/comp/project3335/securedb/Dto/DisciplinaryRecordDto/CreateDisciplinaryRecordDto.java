package hk.polyu.comp.project3335.securedb.Dto.DisciplinaryRecordDto;

import java.time.LocalDate;

public class CreateDisciplinaryRecordDto {
    private Long studentId;
    private Long staffId;
    private LocalDate date;
    private String description;

    // Getters and Setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}