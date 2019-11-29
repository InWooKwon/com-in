package com.example.comin.community;

import java.util.Date;

public class Comment {
    private int idx;
    private int boardIdx;
    private int authorIdx;

    private Date date;
    private String content;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getBoardIdx() {
        return boardIdx;
    }

    public void setBoardIdx(int boardIdx) {
        this.boardIdx = boardIdx;
    }

    public int getAuthorIdx() {
        return authorIdx;
    }

    public void setAuthorIdx(int authorIdx) {
        this.authorIdx = authorIdx;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
