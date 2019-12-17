package com.dubey.get_fit_planner_app;

public class User {
    private int userId;
    private String firstname;
    private String lastname;
    private String age;
    private String height;
    private String weight;
    private String gender;

    public User(int id, String first, String last, String age, String height, String weight, String gender)
    {
        this.userId = id;
        this.firstname = first;
        this.lastname = last;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this. gender = gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getGender() {
        return gender;
    }

    public int getUserId() {
        return userId;
    }

    public String getAge() {
        return age;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getHeight() {
        return height;
    }

    public String getLastname() {
        return lastname;
    }

    public String getWeight() {
        return weight;
    }
}
