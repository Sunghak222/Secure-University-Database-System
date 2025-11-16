package hk.polyu.comp.project3335.securedb.Dto.DisciplinaryRecordDto;

import java.time.LocalDate;

public class UpdateDisciplinaryRecordDto {
    private LocalDate date;
    private String description;

    // Getters and Setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}