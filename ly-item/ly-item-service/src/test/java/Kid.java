public class Kid {
    private Integer age;
    private String name;
    private Boolean sex;
    private School school;

    public void setAge(Integer age) {
        this.age = age;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Kid(Integer age, String name, Boolean sex, School school) {
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.school = school;
    }

    public Kid() {
    }

    public Kid(int age) {
        this.age = age;
    }

    public Kid(int age, String name, Boolean sex) {
        this.age = age;
        this.name = name;
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

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }
}
