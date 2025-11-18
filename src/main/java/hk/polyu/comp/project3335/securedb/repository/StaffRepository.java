package hk.polyu.comp.project3335.securedb.repository;

import hk.polyu.comp.project3335.securedb.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Map;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query(value = """
        SELECT
            id,
            password,
            last_name,
            first_name,
            gender,
            CAST(AES_DECRYPT(identification_number, UNHEX(SHA2(:key,512))) AS CHAR) AS identification_number, 
            CAST(AES_DECRYPT(address, UNHEX(SHA2(:key,512))) AS CHAR) AS address,                          
            email,
            CAST(AES_DECRYPT(phone, UNHEX(SHA2(:key,512))) AS CHAR) AS phone,                              
            department,
            role
        FROM staffs
        WHERE id = :id
        """, nativeQuery = true)
    Optional<Map<String, Object>> findDecryptedById(@Param("id") Long id,
                                                    @Param("key") String key);


    @Query(value = """
        SELECT
            id,
            password,
            last_name,
            first_name,
            gender,
            CAST(AES_DECRYPT(identification_number, UNHEX(SHA2(:key,512))) AS CHAR) AS identification_number,  
            CAST(AES_DECRYPT(address, UNHEX(SHA2(:key,512))) AS CHAR) AS address,                            
            email,
            CAST(AES_DECRYPT(phone, UNHEX(SHA2(:key,512))) AS CHAR) AS phone,                             
            department,
            role
        FROM staffs
        WHERE email = :email
        """, nativeQuery = true)
    Optional<Map<String, Object>> findDecryptedByEmail(@Param("email") String email,
                                                       @Param("key") String key);
}
