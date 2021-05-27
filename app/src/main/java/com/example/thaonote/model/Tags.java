package com.example.thaonote.model;

import java.io.Serializable;

public class Tags implements Serializable {

    private int tagID;
    private String tagTitle;

    public Tags() {
    }

    public Tags(int tagID, String tagTitle) {
        this.tagID = tagID;
        this.tagTitle = tagTitle;
    }

    public Tags(String tagTitle) {
        this.tagTitle = tagTitle;
    }

    public int getTagID() {
        return tagID;
    }

    public void setTagID(int tagID) {
        this.tagID = tagID;
    }

    public String getTagTitle() {
        return tagTitle;
    }

    public void setTagTitle(String tagTitle) {
        this.tagTitle = tagTitle;
    }
}
