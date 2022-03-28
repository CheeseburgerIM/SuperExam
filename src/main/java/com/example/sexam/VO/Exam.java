package com.example.sexam.VO;

import com.example.sexam.entity.module;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class Exam {
    private String teacherUsername;
    private String title;
    private String tips;
    private String startTime;
    private String endTime;
    private String examClass;
    private String course;
    private int duration;
    private double totalScore;
    private int questionCnt;
    private List<module> modules;

    public Exam() {
    }

    public Exam(String teacherUsername, String title, String tips, String startTime, String endTime, String examClass, String course, int duration, double totalScore, int questionCnt, List<module> modules) {
        this.teacherUsername = teacherUsername;
        this.title = title;
        this.tips = tips;
        this.startTime = startTime;
        this.endTime = endTime;
        this.examClass = examClass;
        this.course = course;
        this.duration = duration;
        this.totalScore = totalScore;
        this.questionCnt = questionCnt;
        this.modules = modules;
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

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
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

    public String getExamClass() {
        return examClass;
    }

    public void setExamClass(String examClass) {
        this.examClass = examClass;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public List<module> getModules() {
        return modules;
    }

    public void setModules(List<module> modules) {
        this.modules = modules;
    }
}
