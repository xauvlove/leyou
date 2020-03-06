public class Student {
    private Integer age;
    private String name;
    private School school;
    private Boolean sex;

    public Student() {
    }

    public Student(int age, String name, School school, Boolean sex) {
        this.age = age;
        this.name = name;
        this.school = school;
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }
}
