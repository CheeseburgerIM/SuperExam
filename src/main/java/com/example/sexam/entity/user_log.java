package com.example.sexam.entity;

import com.example.sexam.embed.user_log_key;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_log")
public class user_log {

    @EmbeddedId
    private user_log_key id;

    @Column(name = "content")
    private String content;

    public user_log() {
    }

    public user_log(user_log_key id, String content) {
        this.id = id;
        this.content = content;
    }

    public user_log_key getId() {
        return id;
    }

    public void setId(user_log_key id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
