package com.example.sexam.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "exam")
public class exam {

    @Id
    @Column(name = "eid")
    private String eid;

    @Column(name = "teacherUsername")
    private String teacherUsername;

    @Column(name = "tips")
    private String tips;

    @Column(name = "title")
    private String title;

    @Column(name = "startTime")
    private String startTime;

    @Column(name = "endTime")
    private String endTime;

    @Column(name = "course")
    private String course;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "modulesId")
    private String modulesId;

    @Column(name = "totalScore")
    private double totalScore;

    @Column(name = "questionCnt")
    private int questionCnt;

    public exam() {
    }

    public exam(String eid, String teacherUsername, String tips, String title, String startTime, String endTime, String course, Integer duration, String modulesId, double totalScore, int questionCnt) {
        this.eid = eid;
        this.teacherUsername = teacherUsername;
        this.tips = tips;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.course = course;
        this.duration = duration;
        this.modulesId = modulesId;
        this.totalScore = totalScore;
        this.questionCnt = questionCnt;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getTeacherUsername() {
        return teacherUsername;
    }

    public void setTeacherUsername(String teacherUsername) {
        this.teacherUsername = teacherUsername;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getModulesId() {
        return modulesId;
    }

    public void setModulesId(String modulesId) {
        this.modulesId = modulesId;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public int getQuestionCnt() {
        return questionCnt;
    }

    public void setQuestionCnt(int questionCnt) {
        this.questionCnt = questionCnt;
    }
}
