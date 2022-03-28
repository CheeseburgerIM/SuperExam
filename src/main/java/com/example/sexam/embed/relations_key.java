package com.example.sexam.embed;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class relations_key implements Serializable {


    private String eid;

    private String teacherUsername;

    private String studentUsername;

    public relations_key() {
    }

    public relations_key(String eid, String teacherUsername, String studentUsername) {
        this.eid = eid;
        this.teacherUsername = teacherUsername;
        this.studentUsername = studentUsername;
    }

    public String getTeacherUsername() {
        return teacherUsername;
    }

    public void setTeacherUsername(String teacherUsername) {
        this.teacherUsername = teacherUsername;
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
