package com.example.sexam.embed;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class messages_key implements Serializable {
    private String mid;
    private String username;

    public messages_key() {
    }

    public messages_key(String mid, String username) {
        this.mid = mid;
        this.username = username;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
