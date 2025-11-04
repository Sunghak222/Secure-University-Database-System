package hk.polyu.comp.project3335.securedb.model;

import jakarta.persistence.*;


@Entity
@Table(name="grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Long courseId;

    @Column(nullable = false, length = 10)
    private String term;

    @Column(length = 5)
    private String grade;

    @Column(length = 255)
    private String comments;

    public Grade() {
    }

    public Grade(Long studentId, Long courseId, String term, String grade, String comments) {
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
