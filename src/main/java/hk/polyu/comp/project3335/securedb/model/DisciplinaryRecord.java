package hk.polyu.comp.project3335.securedb.model;

import hk.polyu.comp.project3335.securedb.config.AESEncryptConverter;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "disciplinary_records")
public class DisciplinaryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Long staffId;

    @Convert(converter = AESEncryptConverter.class)
    @Column(length = 500)
    private String descriptions;

    public DisciplinaryRecord() {
    }

    public DisciplinaryRecord(Long studentId, LocalDate date, Long staffId, String descriptions) {
        this.studentId = studentId;
        this.date = date;
        this.staffId = staffId;
        this.descriptions = descriptions;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }
}