package hk.polyu.comp.project3335.securedb.Dto.GradeDto;

public class UpdateGradeDto {

    private String grade;
    private String comments;

    public UpdateGradeDto() {
    }

    public UpdateGradeDto(String term, String grade, String comments) {
        this.grade = grade;
        this.comments = comments;
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


