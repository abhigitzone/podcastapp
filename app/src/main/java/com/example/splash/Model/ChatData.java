package com.example.splash.Model;

public class ChatData {
    private String text;
    private String name;
    private String photoUrl;
    private String chatProfileUrl;

    public ChatData() {
        //This constructor is used for data snapshot call back..
    }

    public ChatData(String text, String name, String photoUrl, String chatProfileUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.chatProfileUrl = chatProfileUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getChatProfileUrl() {
        return chatProfileUrl;
    }

    public void setChatProfileUrl(String chatProfileUrl) {
        this.chatProfileUrl = chatProfileUrl;
    }
}
