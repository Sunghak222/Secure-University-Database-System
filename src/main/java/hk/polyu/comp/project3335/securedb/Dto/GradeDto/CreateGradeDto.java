package hk.polyu.comp.project3335.securedb.Dto.GradeDto;

public class CreateGradeDto {
    
    private Long studentId;
    private Long courseId;
    private String term;
    private String grade;
    private String comments;

    public CreateGradeDto() {
    }

    public CreateGradeDto(Long studentId, Long courseId, String term, String grade, String comments) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.term = term;
        this.grade = grade;
        this.comments = comments;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

