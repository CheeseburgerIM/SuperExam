package com.example.sexam.embed;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class classstudent_key implements Serializable {

    private String username;

    private String cid;

    public classstudent_key() {
    }

    public classstudent_key(String username, String cid) {
        this.username = username;
        this.cid = cid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

}
