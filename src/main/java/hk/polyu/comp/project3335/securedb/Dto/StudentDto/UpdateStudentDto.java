package hk.polyu.comp.project3335.securedb.Dto.StudentDto;

public class UpdateStudentDto {
    
    private String firstName;
    private String lastName;
    private String gender;
    private String identificationNumber;
    private String address;
    private String email;
    private String phone;
    private Integer enrollmentYear;
    private String guardianRelation;

    public UpdateStudentDto() {
    }

    public UpdateStudentDto(String firstName, String lastName, String gender, 
                           String identificationNumber, String address, String email, 
                           String phone, Integer enrollmentYear, String guardianRelation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.identificationNumber = identificationNumber;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.enrollmentYear = enrollmentYear;
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

    public String getGuardianRelation() {
        return guardianRelation;
    }

    public void setGuardianRelation(String guardianRelation) {
        this.guardianRelation = guardianRelation;
    }
}
