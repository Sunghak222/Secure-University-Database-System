package hk.polyu.comp.project3335.securedb.repository;

import hk.polyu.comp.project3335.securedb.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query(
            value = """
            SELECT
                id,
                last_name,
                first_name,
                gender,
                CAST(AES_DECRYPT(identification_number, UNHEX(SHA2(:key,512))) AS CHAR) AS identification_number, 
                CAST(AES_DECRYPT(address, UNHEX(SHA2(:key,512))) AS CHAR) AS address, 
                email,
                CAST(AES_DECRYPT(phone, UNHEX(SHA2(:key,512))) AS CHAR) AS phone,    
                enrollment_year,
                guardian_id,
                guardian_relation
            FROM students
            WHERE id = :id
            """,
            nativeQuery = true
    )
    Optional<Map<String, Object>> findDecryptedById(@Param("id") Long id, @Param("key") String key);

    @Query(value = """
        SELECT
            id,
            last_name,
            first_name,
            gender,
            CAST(AES_DECRYPT(identification_number, UNHEX(SHA2(:key,512))) AS CHAR) AS identification_number, 
            CAST(AES_DECRYPT(address, UNHEX(SHA2(:key,512))) AS CHAR) AS address,                          
            email,
            CAST(AES_DECRYPT(phone, UNHEX(SHA2(:key,512))) AS CHAR) AS phone,                          
            enrollment_year,
            guardian_id,
            guardian_relation
        FROM students
        WHERE email = :email
        """, nativeQuery = true)
    Optional<Map<String, Object>> findDecryptedByEmail(@Param("email") String email, @Param("key") String key);


    @Query(value = """
        SELECT
            id,
            last_name,
            first_name,
            gender,
            CAST(AES_DECRYPT(identification_number, UNHEX(SHA2(:key,512))) AS CHAR) AS identification_number, 
            CAST(AES_DECRYPT(address, UNHEX(SHA2(:key,512))) AS CHAR) AS address,                       
            email,
            CAST(AES_DECRYPT(phone, UNHEX(SHA2(:key,512))) AS CHAR) AS phone,                           
            enrollment_year,
            guardian_id,
            guardian_relation
        FROM students
        WHERE guardian_id = :guardianId
        """, nativeQuery = true)
    List<Map<String, Object>> findDecryptedByGuardianId(@Param("guardianId") Long guardianId, @Param("key") String key);


    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO students (
            last_name, first_name, gender,
            identification_number, address, email, phone,
            enrollment_year, guardian_id, guardian_relation
        ) VALUES (
            :lastName,
            :firstName,
            :gender,
            AES_ENCRYPT(:idNo, UNHEX(SHA2(:key,512))), 
            AES_ENCRYPT(:addr, UNHEX(SHA2(:key,512))),    
            :email,
            AES_ENCRYPT(:phone, UNHEX(SHA2(:key,512))), 
            :enrollYear,
            :guardianId,
            :guardianRelation
        )
        """, nativeQuery = true)
    void encryptAndInsert(
            @Param("lastName") String lastName,
            @Param("firstName") String firstName,
            @Param("gender") String gender,
            @Param("idNo") String idNo,
            @Param("addr") String addr,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("enrollYear") Integer enrollYear,
            @Param("guardianId") Long guardianId,
            @Param("guardianRelation") String guardianRelation,
            @Param("key") String key);

    @Modifying
    @Transactional
    @Query(value = """
    UPDATE students
    SET
        last_name = :lastName,
        first_name = :firstName,
        gender = :gender,
        identification_number = AES_ENCRYPT(:idNo, UNHEX(SHA2(:key,512))),
        address = AES_ENCRYPT(:addr, UNHEX(SHA2(:key,512))),       
        email = :email,
        phone = AES_ENCRYPT(:phone, UNHEX(SHA2(:key,512))),       
        enrollment_year = :enrollYear,
        guardian_relation = :guardianRelation
    WHERE id = :id
    """, nativeQuery = true)
    void updateEncrypted(
            @Param("id") Long id,
            @Param("lastName") String lastName,
            @Param("firstName") String firstName,
            @Param("gender") String gender,
            @Param("idNo") String idNo,
            @Param("addr") String addr,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("enrollYear") Integer enrollYear,
            @Param("guardianRelation") String guardianRelation,
            @Param("key") String key
    );
}
