package com.example.sexam.embed;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class classexam_key implements Serializable {

    private String cid;

    private String eid;

    public classexam_key() {
    }

    public classexam_key(String cid, String eid) {
        this.cid = cid;
        this.eid = eid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }
}
