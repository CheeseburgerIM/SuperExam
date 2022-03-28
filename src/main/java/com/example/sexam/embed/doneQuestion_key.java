package com.example.sexam.embed;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class doneQuestion_key implements Serializable {

    private String qid;

    private String eid;

    private String studentUsername;

    public doneQuestion_key() {
    }

    public doneQuestion_key(String qid, String eid, String studentUsername) {
        this.qid = qid;
        this.eid = eid;
        this.studentUsername = studentUsername;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
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
