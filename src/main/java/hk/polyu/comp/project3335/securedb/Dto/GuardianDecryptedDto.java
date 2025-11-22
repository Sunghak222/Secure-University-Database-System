package hk.polyu.comp.project3335.securedb.Dto;

import java.util.Map;

public class GuardianDecryptedDto {

    private Long id;
    private String lastName;
    private String firstName;
    private String email;
    private String phone;   // decrypted

    public GuardianDecryptedDto(Long id, String lastName, String firstName,
                                String email, String phone) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phone = phone;
    }

    public static GuardianDecryptedDto from(Map<String, Object> row) {
        return new GuardianDecryptedDto(
                ((Number) row.get("id")).longValue(),
                (String) row.get("last_name"),
                (String) row.get("first_name"),
                (String) row.get("email"),
                (String) row.get("phone")
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
}
