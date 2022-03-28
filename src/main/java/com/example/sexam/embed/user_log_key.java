package com.example.sexam.embed;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class user_log_key implements Serializable {

    private String username;

    private String ldate;

    public user_log_key() {
    }

    public user_log_key(String username, String ldate) {
        this.username = username;
        this.ldate = ldate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLdate() {
        return ldate;
    }

    public void setLdate(String ldate) {
        this.ldate = ldate;
    }
}
