package hk.polyu.comp.project3335.securedb.model;

import jakarta.persistence.*;

@Entity
@Table(name="auth_users")
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=100)
    private String email;

    @Column(nullable=false, length=100)
    private String passwordHash;

    @Column(nullable=false, length=16)
    private String role;

    private Long staffId;
    private Long studentId;
    private Long guardianId;

    private Boolean enabled = true;

    public AuthUser() {

    }

    public AuthUser(String email, String passwordHash, String role, Long staffId, Long studentId, Long guardianId, Boolean enabled) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.staffId = staffId;
        this.studentId = studentId;
        this.guardianId = guardianId;
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(Long guardianId) {
        this.guardianId = guardianId;
    }
}
