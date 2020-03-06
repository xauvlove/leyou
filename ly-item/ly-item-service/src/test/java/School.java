public class School {
    private String schoolName;
    private int schoolSize;

    public School() {
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getSchoolSize() {
        return schoolSize;
    }

    public void setSchoolSize(int schoolSize) {
        this.schoolSize = schoolSize;
    }

    public School(String schoolName, int schoolSize) {
        this.schoolName = schoolName;
        this.schoolSize = schoolSize;
    }
}
