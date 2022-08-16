package com.ssevening.bestcuts.object;

public class PipeInfo {

    public PipeInfo(int length, int count) {
        this.length = length;
        this.count = count;
    }

    private int id;
    private int length;
    private int count;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
