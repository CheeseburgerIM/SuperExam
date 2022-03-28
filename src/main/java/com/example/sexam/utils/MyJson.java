package com.example.sexam.utils;

public class MyJson {

    private int status;
    private Object result;
    private String message;

    public MyJson() {
    }

    public MyJson(int status, Object result, String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
