package hk.polyu.comp.project3335.securedb.repository;

import hk.polyu.comp.project3335.securedb.model.DisciplinaryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DisciplinaryRecordRepository extends JpaRepository<DisciplinaryRecord, Long> {

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO disciplinary_records (student_id, date, staff_id, descriptions)
        VALUES (:studentId, :date, :staffId,
                AES_ENCRYPT(:descriptions, UNHEX(SHA2(:key, 512))))
        """, nativeQuery = true)
    void insertEncrypted(
            @Param("studentId") Long studentId,
            @Param("date") LocalDate date,
            @Param("staffId") Long staffId,
            @Param("descriptions") String descriptions,
            @Param("key") String key
    );


    @Modifying
    @Transactional
    @Query(value = """
        UPDATE disciplinary_records
        SET date = :date,
            descriptions = AES_ENCRYPT(:descriptions, UNHEX(SHA2(:key, 512)))
        WHERE id = :id
        """, nativeQuery = true)
    void updateEncrypted(
            @Param("id") Long id,
            @Param("date") LocalDate date,
            @Param("descriptions") String descriptions,
            @Param("key") String key
    );


    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Optional<Long> findLastInserted();


    @Query(value = """
        SELECT
            id,
            student_id,
            staff_id,
            date,
            CAST(AES_DECRYPT(descriptions, UNHEX(SHA2(:key,512))) AS CHAR) AS descriptions
        FROM disciplinary_records
        WHERE id = :id
        """, nativeQuery = true)
    Optional<Map<String, Object>> findDecryptedById(
            @Param("id") Long id,
            @Param("key") String key
    );


    @Query(value = """
        SELECT
            id,
            student_id,
            staff_id,
            date,
            CAST(AES_DECRYPT(descriptions, UNHEX(SHA2(:key,512))) AS CHAR) AS descriptions
        FROM disciplinary_records
        WHERE student_id = :studentId
        """, nativeQuery = true)
    List<Map<String, Object>> findDecryptedByStudentId(
            @Param("studentId") Long studentId,
            @Param("key") String key
    );


    @Query(value = """
        SELECT
            id,
            student_id,
            staff_id,
            date,
            CAST(AES_DECRYPT(descriptions, UNHEX(SHA2(:key,512))) AS CHAR) AS descriptions
        FROM disciplinary_records
        WHERE staff_id = :staffId
        """, nativeQuery = true)
    List<Map<String, Object>> findDecryptedByStaffId(
            @Param("staffId") Long staffId,
            @Param("key") String key
    );

    @Query(value = """
        SELECT
            id,
            student_id,
            staff_id,
            date,
            CAST(AES_DECRYPT(descriptions, UNHEX(SHA2(:key,512))) AS CHAR) AS descriptions
        FROM disciplinary_records
        WHERE student_id = :studentId
          AND staff_id = :staffId
        """, nativeQuery = true)
    List<Map<String, Object>> findDecryptedByStudentIdAndStaffId(
            @Param("studentId") Long studentId,
            @Param("staffId") Long staffId,
            @Param("key") String key
    );

    @Query(value = """
        SELECT
            dr.id AS id,
            dr.student_id AS student_id,
            dr.staff_id AS staff_id,
            dr.date AS date,
            CAST(AES_DECRYPT(dr.descriptions, UNHEX(SHA2(:key,512))) AS CHAR) AS descriptions
        FROM disciplinary_records dr
        """, nativeQuery = true)
    List<Map<String, Object>> findDecryptedAll(
            @Param("key") String key
    );
}
