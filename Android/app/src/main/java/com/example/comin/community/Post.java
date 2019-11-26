package com.example.comin.community;
import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {
    private int idx;
    private int type;
    private String title;
    private int score;
    private String body;


    private Date date;
    private String author;
    private int up;
    private int tag1;
    private int tag2;
    private int tag3;
    private int tag4;
    private int tag5;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getTag1() {
        return tag1;
    }

    public void setTag1(int tag1) {
        this.tag1 = tag1;
    }

    public int getTag2() {
        return tag2;
    }

    public void setTag2(int tag2) {
        this.tag2 = tag2;
    }

    public int getTag3() {
        return tag3;
    }

    public void setTag3(int tag3) {
        this.tag3 = tag3;
    }

    public int getTag4() {
        return tag4;
    }

    public void setTag4(int tag4) {
        this.tag4 = tag4;
    }

    public int getTag5() {
        return tag5;
    }

    public void setTag5(int tag5) {
        this.tag5 = tag5;
    }
}
