package com.teachinghelper;

import java.io.Serializable;

public class TeacherModel implements Serializable {

    private String name;
    private String school;
    private String email;

    public TeacherModel() {
    }

    public TeacherModel(String name, String school, String email) {
        this.name = name;
        this.school = school;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
