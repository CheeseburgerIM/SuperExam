package com.example.sexam.entity;

import com.example.sexam.embed.doneQuestion_key;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "doneQuestion")
public class doneQuestion {

    @EmbeddedId
    private doneQuestion_key id;

    @Column(name = "answer")
    private String answer;

    @Column(name = "score")
    private double score;

    @Column(name = "status")
    private int status;

    @Column(name = "isOvercome")
    private int isOvercome;

    @Column(name = "qtype")
    private int qtype;

    @Column(name = "studentAnswer")
    private String studentAnswer;

    public doneQuestion() {
    }

    public doneQuestion(doneQuestion_key id, String answer, double score, int status, int isOvercome, int qtype, String studentAnswer) {
        this.id = id;
        this.answer = answer;
        this.score = score;
        this.status = status;
        this.isOvercome = isOvercome;
        this.qtype = qtype;
        this.studentAnswer = studentAnswer;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public doneQuestion_key getId() {
        return id;
    }

    public void setId(doneQuestion_key id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsOvercome() {
        return isOvercome;
    }

    public void setIsOvercome(int isOvercome) {
        this.isOvercome = isOvercome;
    }

    public int getQtype() {
        return qtype;
    }

    public void setQtype(int qtype) {
        this.qtype = qtype;
    }
}
