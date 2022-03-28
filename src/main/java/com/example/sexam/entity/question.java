package com.example.sexam.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author Lang wenchong
 * @Date 2022/3/1 19:51
 * @Version 1.0
 */

@Entity
@Table(name = "question")
public class question {
    @Id
    @Column(name = "qid")
    private String qid;

    @Column(name = "teacherUsername")
    private String teacherUsername;

    @Column(name = "teacherName")
    private String teacherName;

    @Column(name = "title")
    private String title;

    @Column(name = "answer")
    private String answer;

    @Column(name = "analysis")
    private String analysis;

    @Column(name = "items")
    private String items;

    @Column(name = "score")
    private double score;

    @Column(name = "difficulty")
    private double difficulty;

    @Column(name = "count")
    private int count;

    @Column(name = "rightCnt")
    private int rightCnt;

    @Column(name = "qtype")
    private int qtype;

    public question() {
    }

    public question(String qid, String teacherUsername, String teacherName, String title, String answer, String analysis, String items, double score, double difficulty, int count, int rightCnt, int qtype) {
        this.qid = qid;
        this.teacherUsername = teacherUsername;
        this.teacherName = teacherName;
        this.title = title;
        this.answer = answer;
        this.analysis = analysis;
        this.items = items;
        this.score = score;
        this.difficulty = difficulty;
        this.count = count;
        this.rightCnt = rightCnt;
        this.qtype = qtype;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getTeacherUsername() {
        return teacherUsername;
    }

    public void setTeacherUsername(String teacherUsername) {
        this.teacherUsername = teacherUsername;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRightCnt() {
        return rightCnt;
    }

    public void setRightCnt(int rightCnt) {
        this.rightCnt = rightCnt;
    }

    public int getQtype() {
        return qtype;
    }

    public void setQtype(int qtype) {
        this.qtype = qtype;
    }
}
