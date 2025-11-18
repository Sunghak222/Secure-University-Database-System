package hk.polyu.comp.project3335.securedb.repository;

import hk.polyu.comp.project3335.securedb.model.Guardian;
import hk.polyu.comp.project3335.securedb.model.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

public interface GuardianRepository extends JpaRepository<Guardian, Long> {

    @Query(value = """
        SELECT
            id,
            last_name,
            first_name,
            email,
            CAST(AES_DECRYPT(phone, UNHEX(SHA2(:key,512))) AS CHAR) AS phone 
        FROM guardians
        WHERE id = :id
        """, nativeQuery = true)
    Optional<Map<String, Object>> findDecryptedById(@Param("id") Long id,
                                                    @Param("key") String key);

    //decrypted guardian
    @Query(value = """
        SELECT
            id,
            last_name,
            first_name,
            email,
            CAST(AES_DECRYPT(phone, UNHEX(SHA2(:key,512))) AS CHAR) AS phone 
        FROM guardians
        WHERE email = :email
        """, nativeQuery = true)
    Optional<Map<String, Object>> findDecryptedByEmail(@Param("email") String email,
                                                       @Param("key") String key);

    @Modifying
    @Transactional
    @Query(value = """
    UPDATE guardians
    SET phone = AES_ENCRYPT(:phone, UNHEX(SHA2(:key,512)))
    WHERE id = :id
    """, nativeQuery = true)
    void updateEncryptedPhone(@Param("id") Long id,
                              @Param("phone") String phone,
                              @Param("key") String key);
}
