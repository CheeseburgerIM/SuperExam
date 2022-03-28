package com.example.sexam.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "module")
public class module {

    @Id
    @Column(name = "mid")
    private String mid;

    @Column(name = "title")
    private String title;

    @Column(name = "questionsId")
    private String questionsId;

    public module() {
    }

    public module(String mid, String title, String questionsId) {
        this.mid = mid;
        this.title = title;
        this.questionsId = questionsId;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestionId() {
        return questionsId;
    }

    @JsonProperty(value = "questionsId")
    public void setQuestionId(String questionId) {
        this.questionsId = questionId;
    }
}
