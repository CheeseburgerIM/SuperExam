package com.example.sexam.VO;

/**
 * @Author Lang wenchong
 * @Date 2022/3/6 14:52
 * @Version 1.0
 */
public class Correct {
    private String qid;
    private int qtype;
    private double score;

    public Correct() {
    }

    public Correct(String qid, int qtype, double score) {
        this.qid = qid;
        this.qtype = qtype;
        this.score = score;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public int getQtype() {
        return qtype;
    }

    public void setQtype(int qtype) {
        this.qtype = qtype;
    }

    public double getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
