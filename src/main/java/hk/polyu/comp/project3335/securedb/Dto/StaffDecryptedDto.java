package hk.polyu.comp.project3335.securedb.Dto;

import java.util.Map;

public class StaffDecryptedDto {

    private Long id;
    private String password;
    private String lastName;
    private String firstName;
    private String gender;
    private String identificationNumber;
    private String address;
    private String email;
    private String phone;
    private String department;
    private String role;

    public StaffDecryptedDto(Long id, String password, String lastName, String firstName,
                             String gender, String identificationNumber, String address,
                             String email, String phone, String department, String role) {

        this.id = id;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.gender = gender;
        this.identificationNumber = identificationNumber;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.role = role;
    }

    public static StaffDecryptedDto from(Map<String, Object> row) {
        return new StaffDecryptedDto(
                ((Number) row.get("id")).longValue(),
                (String) row.get("password"),
                (String) row.get("last_name"),
                (String) row.get("first_name"),
                (String) row.get("gender"),
                (String) row.get("identification_number"),
                (String) row.get("address"),
                (String) row.get("email"),
                (String) row.get("phone"),
                (String) row.get("department"),
                (String) row.get("role")
        );
    }
}
