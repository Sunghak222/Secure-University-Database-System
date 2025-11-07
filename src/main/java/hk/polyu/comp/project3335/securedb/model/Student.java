package hk.polyu.comp.project3335.securedb.model;

import jakarta.persistence.*;

@Entity
@Table(name="students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 50)
    private String gender;

    @Column(nullable = false, length = 50)
    private String identificationNumber;

    @Column(length = 250)
    private String address;

    @Column(unique=true,length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    private Integer enrollmentYear;

    private Long guardianId;

    private String guardianRelation;

    public Student() {
    }

    public Student(String firstName, String lastName, String gender, String identificationNumber,
                   String address, String email, String phone, Integer enrollmentYear,
                   Long guardianId, String guardianRelation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.identificationNumber = identificationNumber;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.enrollmentYear = enrollmentYear;
        this.guardianId = guardianId;
        this.guardianRelation = guardianRelation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
