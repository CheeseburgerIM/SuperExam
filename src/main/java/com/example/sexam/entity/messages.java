package com.example.sexam.entity;


import com.example.sexam.embed.messages_key;

import javax.persistence.*;

@Entity
@Table(name = "messages")
public class messages {

    @EmbeddedId
    private messages_key id;

    @Column(name = "title")
    private String title;

    @Column(name = "mdate")
    private String mdate;

    @Column(name = "content")
    private String content;

    @Column(name = "mstate")
    private int mstate;

    @Column(name = "mtype")
    private String mtype;

    public messages() {
    }

    public messages(messages_key id, String title, String mdate, String content, int mstate, String mtype) {
        this.id = id;
        this.title = title;
        this.mdate = mdate;
        this.content = content;
        this.mstate = mstate;
        this.mtype = mtype;
    }

    public messages_key getId() {
        return id;
    }

    public void setId(messages_key id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMstate() {
        return mstate;
    }

    public void setMstate(int mstate) {
        this.mstate = mstate;
    }

    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }
}
