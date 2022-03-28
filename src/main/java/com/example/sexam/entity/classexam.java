package com.example.sexam.entity;

import com.example.sexam.embed.classexam_key;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "classexam")
public class classexam {

    @EmbeddedId
    private classexam_key id;

    public classexam() {
    }

    public classexam(classexam_key id) {
        this.id = id;
    }

    public classexam_key getId() {
        return id;
    }

    public void setId(classexam_key id) {
        this.id = id;
    }

}
