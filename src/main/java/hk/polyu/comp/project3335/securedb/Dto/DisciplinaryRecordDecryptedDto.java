package hk.polyu.comp.project3335.securedb.Dto;

import java.time.LocalDate;
import java.util.Map;

public class DisciplinaryRecordDecryptedDto {

    private Long id;
    private Long studentId;
    private Long staffId;
    private LocalDate date;
    private String descriptions;

    public DisciplinaryRecordDecryptedDto() {}

    public DisciplinaryRecordDecryptedDto(Long id, Long studentId, Long staffId, LocalDate date, String descriptions) {
        this.id = id;
        this.studentId = studentId;
        this.staffId = staffId;
        this.date = date;
        this.descriptions = descriptions;
    }

    public static DisciplinaryRecordDecryptedDto from(Map<String, Object> row) {
        return new DisciplinaryRecordDecryptedDto(
                ((Number) row.get("id")).longValue(),
                ((Number) row.get("student_id")).longValue(),
                ((Number) row.get("staff_id")).longValue(),
                ((java.sql.Date) row.get("date")).toLocalDate(),
                (String) row.get("descriptions")
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }
}
