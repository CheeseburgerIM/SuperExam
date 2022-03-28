package com.example.sexam.VO;

/**
 * @Author Lang wenchong
 * @Date 2022/3/3 21:07
 * @Version 1.0
 */
public class Question {
    private int qtype;
    private String qid;
    private String answer;

    public Question() {
    }

    public Question(int qtype, String qid, String answer) {
        this.qtype = qtype;
        this.qid = qid;
        this.answer = answer;
    }

    public int getQtype() {
        return qtype;
    }

    public void setQtype(int qtype) {
        this.qtype = qtype;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
