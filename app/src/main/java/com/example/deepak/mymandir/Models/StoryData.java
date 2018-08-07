package com.example.deepak.mymandir.Models;

/**
 * Created by Sharmaji on 6/2/2018.
 */

public class StoryData {

    private String text;
    private String title;
    private int likeCount;
    private int commentCount;
    private int shareCount;
    private AttachData attachData;
    private UserData userData;

    public StoryData() {
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AttachData getAttachData() {
        return attachData;
    }

    public void setAttachData(AttachData attachData) {
        this.attachData = attachData;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

}
