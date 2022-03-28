package com.example.sexam.embed;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class screenshot_key implements Serializable {

    private String eid;

    private String studentUsername;

    public screenshot_key() {
    }

    public screenshot_key(String eid, String studentUsername) {
        this.eid = eid;
        this.studentUsername = studentUsername;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }
}
