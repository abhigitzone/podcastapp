package com.example.splash.Model;
//Created this model class in order to read data from Real-time database in Explore Activity.
public class StudioData {
    String videoTitle;
    String videoDesc;
    String search;
    String videoURL;
    String videoProfileImg;

    public StudioData() {
    }

    public StudioData(String videoTitle, String videoDesc, String search, String videoURL, String videoProfileImg) {
        this.videoTitle = videoTitle;
        this.videoDesc = videoDesc;
        this.search = search;
        this.videoURL = videoURL;
        this.videoProfileImg = videoProfileImg;
    }

    public String getVideoProfileImg() {
        return videoProfileImg;
    }

    public void setVideoProfileImg(String videoProfileImg) {
        this.videoProfileImg = videoProfileImg;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDesc() {
        return videoDesc;
    }

    public void setVideoDesc(String videoDesc) {
        this.videoDesc = videoDesc;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }
}
