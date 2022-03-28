package com.example.sexam.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_info")
public class user_info {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "school")
    private String school;

    @Column(name = "sex")
    private String sex;

    @Column(name = "avatarPath")
    private String avatarPath;

    @Column(name = "registerTime")
    private String registerTime;

    @Column(name = "warTimes")
    private int warTimes;

    @Column(name = "questions")
    private int questions;

    @Column(name = "overcomes")
    private int overcomes;

    @Column(name = "accuracy")
    private int accuracy;

    @Column(name = "averagePoint")
    private int averagePoint;

    @Column(name = "velocity")
    private int velocity;

    @Column(name = "excellent")
    private int excellent;

    public user_info() {
    }

    public user_info(String username, String email, String name, String phoneNumber, String school, String sex, String avatarPath, String registerTime, int warTimes, int questions, int overcomes, int accuracy, int averagePoint, int velocity, int excellent) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.school = school;
        this.sex = sex;
        this.avatarPath = avatarPath;
        this.registerTime = registerTime;
        this.warTimes = warTimes;
        this.questions = questions;
        this.overcomes = overcomes;
        this.accuracy = accuracy;
        this.averagePoint = averagePoint;
        this.velocity = velocity;
        this.excellent = excellent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public int getWarTimes() {
        return warTimes;
    }

    public void setWarTimes(int warTimes) {
        this.warTimes = warTimes;
    }

    public int getQuestions() {
        return questions;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public int getOvercomes() {
        return overcomes;
    }

    public void setOvercomes(int overcomes) {
        this.overcomes = overcomes;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getAveragePoint() {
        return averagePoint;
    }

    public void setAveragePoint(int averagePoint) {
        this.averagePoint = averagePoint;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getExcellent() {
        return excellent;
    }

    public void setExcellent(int excellent) {
        this.excellent = excellent;
    }
}
