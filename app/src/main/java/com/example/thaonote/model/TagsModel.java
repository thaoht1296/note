package com.example.thaonote.model;

import java.io.Serializable;

public class TagsModel implements Serializable {

    private int tagID;
    private String tagTitle;

    private int tagUser;

    public TagsModel() {
    }

    public TagsModel(int tagID, String tagTitle) {
        this.tagID = tagID;
        this.tagTitle = tagTitle;
    }

    public TagsModel(int tagID, String tagTitle, int tagUser) {
        this.tagID = tagID;
        this.tagTitle = tagTitle;
        this.tagUser = tagUser;
    }

    public TagsModel(String tagTitle, int tagUser) {
        this.tagTitle = tagTitle;
        this.tagUser = tagUser;
    }

    public TagsModel(String tagTitle) {
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

    public int getTagUser() {
        return tagUser;
    }

    public void setTagUser(int tagUser) {
        this.tagUser = tagUser;
    }
}
