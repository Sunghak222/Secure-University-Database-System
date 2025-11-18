package hk.polyu.comp.project3335.securedb.Dto;

import java.util.Map;

public class StudentDecryptedDto {
    private Long id;
    private String lastName;
    private String firstName;
    private String gender;
    private String identificationNumber;
    private String address;
    private String email;
    private String phone;
    private Integer enrollmentYear;
    private Long guardianId;
    private String guardianRelation;

    public StudentDecryptedDto(Long id, String lastName, String firstName,
                               String gender, String identificationNumber,
                               String address, String email, String phone,
                               Integer enrollmentYear, Long guardianId,
                               String guardianRelation) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.gender = gender;
        this.identificationNumber = identificationNumber;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.enrollmentYear = enrollmentYear;
        this.guardianId = guardianId;
        this.guardianRelation = guardianRelation;
    }

    public static StudentDecryptedDto from(Map<String, Object> row) {
        return new StudentDecryptedDto(
                ((Number) row.get("id")).longValue(),
                (String) row.get("last_name"),
                (String) row.get("first_name"),
                (String) row.get("gender"),
                (String) row.get("identification_number"),
                (String) row.get("address"),
                (String) row.get("email"),
                (String) row.get("phone"),
                row.get("enrollment_year") == null ? null : ((Number) row.get("enrollment_year")).intValue(),
                row.get("guardian_id") == null ? null : ((Number) row.get("guardian_id")).longValue(),
                (String) row.get("guardian_relation")
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(Integer enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public Long getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(Long guardianId) {
        this.guardianId = guardianId;
    }

    public String getGuardianRelation() {
        return guardianRelation;
    }

    public void setGuardianRelation(String guardianRelation) {
        this.guardianRelation = guardianRelation;
    }
}
