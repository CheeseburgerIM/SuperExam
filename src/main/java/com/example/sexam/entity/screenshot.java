package com.example.sexam.entity;

import com.example.sexam.embed.screenshot_key;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "screenshot")
public class screenshot {

    @EmbeddedId
    private screenshot_key id;

    @Column(name = "imagePath")
    private String imagePath;

    public screenshot() {
    }

    public screenshot(screenshot_key id, String imagePath) {
        this.id = id;
        this.imagePath = imagePath;
    }

    public screenshot_key getId() {
        return id;
    }

    public void setId(screenshot_key id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
