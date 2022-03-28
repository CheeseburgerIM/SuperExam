package com.example.sexam.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author Lang wenchong
 * @Date 2022/3/1 20:05
 * @Version 1.0
 */

@Entity
@Table(name = "classes")
public class classes {

    @Id
    @Column(name = "cid")
    private String cid;

    @Column(name = "username")
    private String username;

    @Column(name = "cname")
    private String cname;

    @Column(name="teacherName")
    private String teeacherName;

    @Column(name="studentNumber")
    private int studentNumber;

    @Column(name="studentList")
    private String studentList;

    @Column(name="command")
    private String command;

    @Column(name="examCnt")
    private int examCnt;

    public classes() {
    }

    public classes(String cid, String username, String cname, String teeacherName, int studentNumber, String studentList, String command, int examCnt) {
        this.cid = cid;
        this.username = username;
        this.cname = cname;
        this.teeacherName = teeacherName;
        this.studentNumber = studentNumber;
        this.studentList = studentList;
        this.command = command;
        this.examCnt = examCnt;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getTeeacherName() {
        return teeacherName;
    }

    public void setTeeacherName(String teeacherName) {
        this.teeacherName = teeacherName;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentList() {
        return studentList;
    }

    public void setStudentList(String studentList) {
        this.studentList = studentList;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getExamCnt() {
        return examCnt;
    }

    public void setExamCnt(int examCnt) {
        this.examCnt = examCnt;
    }

}
