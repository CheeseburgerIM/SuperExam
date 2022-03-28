package com.example.sexam.entity;

import com.example.sexam.embed.user_calendar_key;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_calendar")
public class user_calendar {

    @EmbeddedId
    private user_calendar_key id;

    @Column(name = "activity")
    private int activity;

    public user_calendar() {
    }

    public user_calendar(user_calendar_key id, int activity) {
        this.id = id;
        this.activity = activity;
    }

    public user_calendar_key getId() {
        return id;
    }

    public void setId(user_calendar_key id) {
        this.id = id;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }
}
