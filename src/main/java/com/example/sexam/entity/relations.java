package com.example.sexam.entity;

import com.example.sexam.embed.relations_key;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "relations")
public class relations {

    @EmbeddedId
    private relations_key id;

    @Column(name = "score")
    private double score;

    @Column(name = "startTime")
    private String startTime;

    @Column(name = "submitTime")
    private String submitTime;

    @Column(name = "rightCnt")
    private int rightCnt;

    @Column(name = "examState")
    private int examState;

    public relations() {
    }

    public relations(relations_key id, double score, String startTime, String submitTime, int rightCnt, int examState) {
        this.id = id;
        this.score = score;
        this.startTime = startTime;
        this.submitTime = submitTime;
        this.rightCnt = rightCnt;
        this.examState = examState;
    }

    public relations_key getId() {
        return id;
    }

    public void setId(relations_key id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public int getRightCnt() {
        return rightCnt;
    }

    public void setRightCnt(int rightCnt) {
        this.rightCnt = rightCnt;
    }

    public int getExamState() {
        return examState;
    }

    public void setExamState(int examState) {
        this.examState = examState;
    }
}
