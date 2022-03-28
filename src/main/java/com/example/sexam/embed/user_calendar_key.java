package com.example.sexam.embed;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class user_calendar_key implements Serializable {

    private String username;

    private String cdate;

    public user_calendar_key() {
    }

    public user_calendar_key(String username, String cdate) {
        this.username = username;
        this.cdate = cdate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }
}
