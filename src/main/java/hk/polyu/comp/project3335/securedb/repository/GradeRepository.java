package hk.polyu.comp.project3335.securedb.repository;

import hk.polyu.comp.project3335.securedb.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentIdAndTerm(long studentId, String term);
    Optional<Grade> findByStudentIdAndCourseId(Long studentId, Long courseId);

    @Query(value = """
        SELECT
            id,
            student_id,
            course_id,
            term,
            CAST(AES_DECRYPT(grade, UNHEX(SHA2(:key,512))) AS CHAR) AS grade,       
            CAST(AES_DECRYPT(comments, UNHEX(SHA2(:key,512))) AS CHAR) AS comments  
        FROM grades
        WHERE id = :id
        """, nativeQuery = true)
    Optional<Map<String,Object>> findDecryptedById(
            @Param("id") Long id,
            @Param("key") String key
    ); // Changed



    @Query(value = """
        SELECT
            id,
            student_id,
            course_id,
            term,
            CAST(AES_DECRYPT(grade, UNHEX(SHA2(:key,512))) AS CHAR) AS grade,     
            CAST(AES_DECRYPT(comments, UNHEX(SHA2(:key,512))) AS CHAR) AS comments 
        FROM grades
        WHERE student_id = :studentId
        """, nativeQuery = true)
    List<Map<String,Object>> findDecryptedByStudentId(
            @Param("studentId") Long studentId,
            @Param("key") String key
    );



    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO grades (student_id, course_id, term, grade, comments)
        VALUES (
            :studentId,
            :courseId,
            :term,
            AES_ENCRYPT(:grade, UNHEX(SHA2(:key,512))),     
            AES_ENCRYPT(:comments, UNHEX(SHA2(:key,512)))     
        )
        """, nativeQuery = true)
    void insertEncryptedGrade(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId,
            @Param("term") String term,
            @Param("grade") String grade,
            @Param("comments") String comments,
            @Param("key") String key
    );


    @Modifying
    @Transactional
    @Query(value = """
        UPDATE grades
        SET 
            grade = AES_ENCRYPT(:grade, UNHEX(SHA2(:key,512))),     
            comments = AES_ENCRYPT(:comments, UNHEX(SHA2(:key,512)))  
        WHERE id = :id
        """, nativeQuery = true)
    void updateEncryptedGrade(
            @Param("id") Long id,
            @Param("grade") String grade,
            @Param("comments") String comments,
            @Param("key") String key
    );

    @Query(value = """
    SELECT
        id,
        student_id,
        course_id,
        term,
        CAST(AES_DECRYPT(grade, UNHEX(SHA2(:key,512))) AS CHAR) AS grade,    
        CAST(AES_DECRYPT(comments, UNHEX(SHA2(:key,512))) AS CHAR) AS comments   
    FROM grades
    WHERE student_id = :studentId
      AND term = :term
    """, nativeQuery = true)
    List<Map<String, Object>> findDecryptedByStudentIdAndTerm(@Param("studentId") Long studentId,
                                                              @Param("term") String term,
                                                              @Param("key") String key
    );

    @Query(value = """
        SELECT
            id,
            student_id,
            course_id,
            term,
            CAST(AES_DECRYPT(grade, UNHEX(SHA2(:key,512))) AS CHAR) AS grade,       
            CAST(AES_DECRYPT(comments, UNHEX(SHA2(:key,512))) AS CHAR) AS comments 
        FROM grades
        WHERE student_id = :studentId
          AND course_id = :courseId
        LIMIT 1
        """, nativeQuery = true)
    Optional<Map<String, Object>> findDecryptedByStudentIdAndCourseId(@Param("studentId") Long studentId,
                                                                      @Param("courseId") Long courseId,
                                                                      @Param("key") String key
    );
}
