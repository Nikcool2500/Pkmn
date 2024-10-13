package ru.mirea.chirkans.pkmn;

public class Student {
    private String firstName;
    private String surName;
    private String fatherName;
    private String group;

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", surName='" + surName + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", group='" + group + '\'' +
                '}';
    }

    public Student() {
    }

    public Student(String surName, String firstName, String familyName, String group) {
        this.firstName = firstName;
        this.surName = surName;
        this.fatherName = familyName;
        this.group = group;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
