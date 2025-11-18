package hk.polyu.comp.project3335.securedb.Dto;

import java.util.Map;

public class GradeDecryptedDto {

    private Long id;
    private Long studentId;
    private Long courseId;
    private String term;
    private String grade;
    private String comments;

    public GradeDecryptedDto(Long id, Long studentId, Long courseId,
                             String term, String grade, String comments) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.term = term;
        this.grade = grade;
        this.comments = comments;
    }

    public static GradeDecryptedDto from(Map<String, Object> row) {
        return new GradeDecryptedDto(
                ((Number) row.get("id")).longValue(),
                ((Number) row.get("student_id")).longValue(),
                ((Number) row.get("course_id")).longValue(),
                (String) row.get("term"),
                (String) row.get("grade"),
                (String) row.get("comments")
        );
    }

    // getters (optional: if needed by JSON serializer)
    public Long getId() { return id; }
    public Long getStudentId() { return studentId; }
    public Long getCourseId() { return courseId; }
    public String getTerm() { return term; }
    public String getGrade() { return grade; }
    public String getComments() { return comments; }
}
