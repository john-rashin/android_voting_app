package com.example.androidvotingsystem;

public class User {
    private String course,block,schoolId;

    public User() {
    }

    public User(String course, String block, String schoolId) {
        this.course = course;
        this.block = block;
        this.schoolId = schoolId;
    }



    public String getCourse() {
        return course;
    }

    public String getBlock() {
        return block;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
}
